package vn.metamon.app.presentation.utils

import androidx.fragment.app.FragmentManager
import vn.metamon.app.di.AppScoped
import vn.metamon.app.presentation.NavHost
import vn.metamon.app.presentation.main.MainFragment
import javax.inject.Inject

@AppScoped
class Navigator @Inject constructor() {
    private fun ensureSingleInstance(host: NavHost, tag: String) {
        val fm = host.navHostFragmentManager()
        fm.findFragmentByTag(tag)?.let {
            fm.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    fun navigateToMainAndFinish(host: NavHost?) {
        host?.let {
            host.navHostFragmentManager().apply {
                popBackStack()
                beginTransaction()
                    .replace(host.containerId(), MainFragment.newInstance(), MainFragment.TAG)
                    .commit()
            }
        }
    }
}