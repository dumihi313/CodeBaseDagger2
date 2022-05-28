package vn.metamon.app.presentation

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager

interface NavHost {
    @IdRes
    fun containerId(): Int

    fun navHostFragmentManager(): FragmentManager

    fun containerView(): View
}