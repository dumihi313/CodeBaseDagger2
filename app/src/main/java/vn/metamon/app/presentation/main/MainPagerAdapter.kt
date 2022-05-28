package vn.metamon.app.presentation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import vn.metamon.app.presentation.FragmentB
import vn.metamon.app.presentation.FragmentC
import vn.metamon.app.presentation.FragmentD
import vn.metamon.app.presentation.home.HomeFragment
import vn.metamon.app.presentation.main.MainTabLayout.TabPosition.*
import javax.inject.Inject

class MainPagerAdapter @Inject constructor(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val count: Int = 0
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return count
    }
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            //todo: need to edit fragment name
            Tab2.index -> FragmentB.newInstance()
            Tab3.index -> FragmentC.newInstance()
            Tab4.index -> FragmentD.newInstance()
            else -> HomeFragment.newInstance()
        }
    }
}