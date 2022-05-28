package vn.metamon.app.presentation.home

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import vn.metamon.app.presentation.home.following.FollowingFragment
import vn.metamon.app.presentation.home.space.SpaceFragment
import vn.metamon.app.utils.notIn

class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val items = mutableListOf<Fragment>()

    private val titles = mutableListOf<String>()

    init {
        resetData()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        if (position notIn items.indices) {
            return -1
        }

        return items[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return items.any { it.hashCode().toLong() == itemId }
    }

    fun getPageTitle(position: Int): String {
        return titles[position]
    }

    private fun resetData() {
        items.clear()
        items.add(SpaceFragment.newInstance())
        items.add(FollowingFragment.newInstance())

        titles.clear()
        titles.add("Kingdom")
        titles.add("Other")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun reset() {
        resetData()
        notifyDataSetChanged()
    }
}