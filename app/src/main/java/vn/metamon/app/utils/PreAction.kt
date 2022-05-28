package vn.metamon.app.utils

import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import vn.metamon.app.presentation.login.LoginDialog
import vn.metamon.app.presentation.login.LoginResultViewModel
import vn.metamon.data.MetamonUserManager
import java.lang.ref.WeakReference

abstract class PreActionClickListener {
    private var actualClickListener: View.OnClickListener? = null

    private var viewRef: WeakReference<View>? = null

    fun onClick(v: View?, actualClickListener: View.OnClickListener?): Boolean {
        this.actualClickListener = actualClickListener
        v?.let { viewRef = WeakReference(v) }
        val shouldBlockActual = executePreAction()
        if (!shouldBlockActual) {
            actualClickListener?.onClick(v)
        }
        return shouldBlockActual
    }

    protected fun invokeActualListener() {
        actualClickListener?.onClick(viewRef?.get())
    }

    protected abstract fun executePreAction(): Boolean
}

class LoginPreActionListener : PreActionClickListener {

    companion object {
        private const val LOGIN_PRECONDITION = "login_precondition"
    }

    private val userManager: MetamonUserManager

    private val fragment: Fragment?

    private val activity: AppCompatActivity?

    constructor(
        userManager: MetamonUserManager,
        fragment: Fragment
    ) : super() {
        this.userManager = userManager
        this.fragment = fragment
        activity = null

        val vm = fragment.viewModels<LoginResultViewModel>(ownerProducer = { fragment }).value
        vm.loginResult.observe(fragment.viewLifecycleOwner) {
            if (it) {
                invokeActualListener()
            }
        }
    }

    constructor(
        userManager: MetamonUserManager,
        activity: AppCompatActivity
    ) : super() {
        this.userManager = userManager
        this.activity = activity
        fragment = null

        val vm = activity.viewModels<LoginResultViewModel>().value
        vm.loginResult.observe(activity) {
            if (it) {
                invokeActualListener()
            }
        }
    }

    override fun executePreAction(): Boolean {
        if (userManager.currentUser.credential.accessToken.isEmpty()) {
            requestLogin()
            return true
        }
        return false
    }

    private fun requestLogin() {
        val dialog = LoginDialog.newInstance()

        fragment?.let {
            it.showDialog(dialog, LOGIN_PRECONDITION)
            return
        }

        activity?.let {
            it.showDialog(dialog, LOGIN_PRECONDITION)
            return
        }
    }
}
