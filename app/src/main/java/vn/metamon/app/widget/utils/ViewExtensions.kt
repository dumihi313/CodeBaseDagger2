package vn.metamon.app.widget.utils

import android.animation.Animator
import android.view.KeyEvent
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import vn.metamon.app.widget.SimpleAnimatorListener

fun View.visible() = run { visibility = View.VISIBLE }

fun View.gone() = run { visibility = View.GONE }

fun View.invisible() = run { visibility = View.INVISIBLE }

fun View.doOnBackPressed(block: () -> Boolean) {
    setOnKeyListener { _, keyCode, _ ->
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return@setOnKeyListener block.invoke()
        }
        return@setOnKeyListener false
    }
}


fun ViewPager.addSimplePageChangeListener(
    onScrollStateChanged: ((state: Int) -> Unit)? = null,
    onPageScrolled: ((
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) -> Unit)? = null,
    onPageSelected: ((position: Int) -> Unit)? = null
) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            onScrollStateChanged?.let { it(state) }
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            onPageScrolled?.let { it(position, positionOffset, positionOffsetPixels) }
        }

        override fun onPageSelected(position: Int) {
            onPageSelected?.let { it(position) }
        }
    })
}

fun TabLayout.addSimpleTabSelectedListener(
    onTabReselected: ((TabLayout.Tab?) -> Unit)? = null,
    onTabUnselected: ((TabLayout.Tab?) -> Unit)? = null,
    onTabSelected: ((TabLayout.Tab?) -> Unit)? = null
) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(p0: TabLayout.Tab?) {
            onTabReselected?.let { it(p0) }
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {
            onTabUnselected?.let { it(p0) }
        }

        override fun onTabSelected(p0: TabLayout.Tab?) {
            onTabSelected?.let { it(p0) }
        }

    })
}

fun ViewPropertyAnimator.setOnEndAnimationListener(onEndAnimationListener : (() -> Unit)? = null) : ViewPropertyAnimator {
    this.setListener(object : SimpleAnimatorListener() {
        override fun onAnimationEnd(animation: Animator?) {
            onEndAnimationListener?.invoke()
        }
    })
    return this
}