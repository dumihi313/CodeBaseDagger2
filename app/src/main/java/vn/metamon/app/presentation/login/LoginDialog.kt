package vn.metamon.app.presentation.login

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.WindowManager.LayoutParams.MATCH_PARENT
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.dialog_login.*
import vn.metamon.R
import vn.metamon.app.presentation.utils.LoginManager
import vn.metamon.data.model.MetaResult
import vn.metamon.data.model.User
import vn.metamon.utils.event.EventDispatcher
import vn.metamon.utils.event.Events

import javax.inject.Inject

private const val LOGIN_FACEBOOK = 0
private const val LOGIN_GOOGLE = 1
private const val LOGIN_EMAIL = 2

private const val SWITCH_ENVIRONMENT_HIT_COUNT = 8

class LoginDialog : DaggerDialogFragment() {
    companion object {
        fun newInstance(): LoginDialog {
            val fragment = LoginDialog()
            fragment.arguments = bundleOf(
            )
            return fragment
        }
    }

    @Inject
    lateinit var appContext: Context

    @Inject
    lateinit var socialManagerFactory: LoginManager.Factory

    private val facebookLoginManager by lazy { socialManagerFactory.create(User.LoginFrom.FACEBOOK) }

    private val googleLoginManager by lazy { socialManagerFactory.create(User.LoginFrom.GOOGLE) }

    private val loginFacebookCallback = SocialLoginCallback(LOGIN_FACEBOOK)

    private val loginGoogleCallback = SocialLoginCallback(LOGIN_GOOGLE)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<LoginViewModel> { viewModelFactory }

    private val loginResultViewModel by viewModels<LoginResultViewModel>(ownerProducer = { requireParentFragment() })

    private var currentSwitchEnvironmentHitCount = SWITCH_ENVIRONMENT_HIT_COUNT

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                cancel()
                return@setOnKeyListener true
            }
            false
        }

        setupAgreementText()

        closeBtn.setOnClickListener { cancel() }

        loginFacebookBtn.setOnClickListener {
//            facebookLoginManager?.doLogin(this, loginFacebookCallback)
        }
        loginGoogleBtn.setOnClickListener {
//            googleLoginManager?.doLogin(this, loginGoogleCallback)
        }

        loginEmailBtn.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passInput.text.toString()
            if (emailInput.text.isEmpty() || passInput.text.isEmpty()) {
                Toast.makeText(
                    it.context,
                    "Invalid input",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            viewModel.loginEmail(email, password)
        }

        switchEnvironmentBtn.setOnClickListener {
            if (currentSwitchEnvironmentHitCount > 0) {
                val last = currentSwitchEnvironmentHitCount
                currentSwitchEnvironmentHitCount--
                if (currentSwitchEnvironmentHitCount < 4) {
                    Toast.makeText(
                        it.context,
                        "Switch environment in $last more click",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
//                EventDispatcher.getInstance().post(Events.SWITCH_ENVIRONMENT)
//                activity?.let { a ->
//                    a.finish()
//                    a.startActivity(SplashScreenActivity.newIntent(a))
//                }
//                currentSwitchEnvironmentHitCount = SWITCH_ENVIRONMENT_HIT_COUNT
            }
        }

        viewModel.loginResult.observe(viewLifecycleOwner) {
            if (it is MetaResult.Success) {
                onLoginSuccess()
            } else if (it is MetaResult.Error) {
                onLoginFailure(it.throwable.toString())
            }
        }
    }

    private fun setupAgreementText() {
    }

    private fun cancel() {
        dismiss()
        Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialog?.window?.let {
            it.setLayout(MATCH_PARENT, MATCH_PARENT)
            it.setBackgroundDrawable(null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (facebookLoginManager?.handleLoginResult(requestCode, resultCode, data) == true) {
            return
        }

        if (googleLoginManager?.handleLoginResult(requestCode, resultCode, data) == true) {
            return
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onLoginFacebookSuccess(token: String) {
        viewModel.loginFacebook(token)
    }

    private fun onLoginGoogleSuccess(token: String) {
        viewModel.loginGoogle(token)
    }

    private fun onLoginSuccess() {
        EventDispatcher.getInstance().post(Events.USER_LOGGED_IN)
        loginResultViewModel.setLoginResult(true)
        dismiss()
    }

    private fun onLoginFailure(cause: String) {
        loginResultViewModel.setLoginResult(false)
        Toast.makeText(context, cause, Toast.LENGTH_SHORT).show()
    }

    private inner class SocialLoginCallback(
        private val loginType: Int
    ) : LoginManager.Callback {
        override fun onResult(result: LoginManager.Result, data: String?, throwable: Throwable?) {
            if (result == LoginManager.Result.SUCCESS && !data.isNullOrEmpty()) {
                when (loginType) {
                    LOGIN_FACEBOOK -> onLoginFacebookSuccess(data)
                    LOGIN_GOOGLE -> onLoginGoogleSuccess(data)
                }
            } else {
                val message = when (result) {
                    LoginManager.Result.CANCEL -> getString(R.string.login_cancel_message)
                    else -> throwable.toString()
                }
                onLoginFailure(message)
            }
        }
    }
}