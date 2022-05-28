package vn.metamon.app.presentation.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import vn.metamon.app.di.FACEBOOK_LOGIN_MANAGER
import vn.metamon.app.di.GOOGLE_LOGIN_MANAGER
import vn.metamon.data.model.User
import vn.metamon.app.presentation.utils.LoginManager.Result.*
import vn.metamon.utils.NetworkStateManager
import javax.inject.Inject
import javax.inject.Named
import vn.metamon.R

interface LoginManager {
    fun doLogin(fragment: Fragment, callback: Callback)

    fun doLogin(activity: Activity, callback: Callback)

    fun doLogout()

    fun handleLoginResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean

    interface Callback {
        fun onResult(result: Result, data: String? = null, throwable: Throwable? = null)
    }

    enum class Result {
        SUCCESS,
        FAILURE,
        CANCEL
    }

    interface Factory {
        fun create(from: User.LoginFrom): LoginManager?
    }
}

class SocialLoginFactory @Inject constructor(
    private val context: Context,
    private val networkStateManager: NetworkStateManager
) : LoginManager.Factory {
    override fun create(from: User.LoginFrom): LoginManager? {
        return when (from) {
            User.LoginFrom.FACEBOOK -> FacebookLoginManager()
            User.LoginFrom.GOOGLE -> GoogleLoginManager(context, networkStateManager)
            else -> null
        }
    }
}

@Named(FACEBOOK_LOGIN_MANAGER)
class FacebookLoginManager @Inject constructor(
) : LoginManager {
    private val loginManager by lazy { com.facebook.login.LoginManager.getInstance() }

    private val permissions = listOf("public_profile", "email")

    private val callbackManager = CallbackManager.Factory.create()

    private val facebookLoginCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult?) {
            val token = AccessToken.getCurrentAccessToken().token
            if (token.isEmpty()) {
                loginCallback?.onResult(
                    FAILURE,
                    throwable = Throwable("Token is empty")
                )
                return
            }
            loginCallback?.onResult(SUCCESS, token)
        }

        override fun onCancel() {
            loginCallback?.onResult(CANCEL)
        }

        override fun onError(error: FacebookException?) {
            loginCallback?.onResult(FAILURE, throwable = error)
        }
    }

    private var loginCallback: LoginManager.Callback? = null

    init {
        loginManager.registerCallback(callbackManager, facebookLoginCallback)
    }

    override fun doLogin(fragment: Fragment, callback: LoginManager.Callback) {
        this.loginCallback = callback
        loginManager.logIn(fragment, permissions)
    }

    override fun doLogin(activity: Activity, callback: LoginManager.Callback) {
        loginCallback = callback
        loginManager.logIn(activity, permissions)
    }

    override fun doLogout() {
        loginManager.logOut()
    }

    override fun handleLoginResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return data?.let { callbackManager.onActivityResult(requestCode, resultCode, it) } ?: false
    }
}

private const val GOOGLE_SIGN_IN_REQUEST_CODE = 1831

@Named(GOOGLE_LOGIN_MANAGER)
class GoogleLoginManager @Inject constructor(
    private val context: Context,
    private val networkStateManager: NetworkStateManager
) : LoginManager {

    private val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//        .requestIdToken(context.getString(R.string.google_client_id))
        .requestIdToken("hieudmhardcode")
        .requestEmail()
        .build()

    private val signInClient = GoogleSignIn.getClient(context, signInOptions)

    private var loginCallback: LoginManager.Callback? = null

    override fun doLogin(fragment: Fragment, callback: LoginManager.Callback) {
        loginCallback = callback

        if (signInClient.signInIntent.resolveActivity(context.packageManager) == null) {
            loginCallback?.onResult(
                FAILURE,
                throwable = Throwable("Cannot connect to Google play services")
            )
            return
        }

        fragment.startActivityForResult(signInClient.signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    override fun doLogin(activity: Activity, callback: LoginManager.Callback) {
        loginCallback = callback

        if (signInClient.signInIntent.resolveActivity(context.packageManager) == null) {
            loginCallback?.onResult(
                FAILURE,
                throwable = Throwable("Cannot connect to Google play services")
            )
            return
        }

        activity.startActivityForResult(signInClient.signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    override fun doLogout() {
        signInClient.signOut()
    }

    override fun handleLoginResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            return when (resultCode) {
                Activity.RESULT_OK -> {
                    handleResult(GoogleSignIn.getSignedInAccountFromIntent(data))
                    true
                }
                Activity.RESULT_CANCELED -> {
                    loginCallback?.onResult(CANCEL)
                    true
                }
                else -> false
            }
        }
        return false
    }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        try {
            val signInAccount = task.getResult(ApiException::class.java)
            if (signInAccount != null) {
                val idToken = signInAccount.idToken
                if (idToken.isNullOrEmpty()) {
                    loginCallback?.onResult(
                        FAILURE,
                        throwable = Throwable(
                            context.getString(
                                R.string.message_login_fail,
                                "Google",
                                context.getString(R.string.login_failure_reason_no_token)
                            )
                        )
                    )
                } else {
                    loginCallback?.onResult(SUCCESS, idToken)
                }
            } else {
                loginCallback?.onResult(
                    FAILURE,
                    throwable = Throwable(
                        context.getString(
                            R.string.message_login_fail,
                            "Google",
                            context.getString(R.string.login_failure_reason_no_account)
                        )
                    )
                )
            }

        } catch (e: ApiException) {
            if (networkStateManager.isAvailable) {
                loginCallback?.onResult(
                    FAILURE,
                    throwable = Throwable(
                        context.getString(
                            R.string.message_login_fail,
                            "Google",
                            e.toString()
                        )
                    )
                )
            } else {
                loginCallback?.onResult(
                    FAILURE,
                    throwable = Throwable(
                        context.getString(
                            R.string.message_login_fail,
                            "Google",
                            context.getString(R.string.no_network_connection)
                        )
                    )
                )
            }
        }
    }
}
