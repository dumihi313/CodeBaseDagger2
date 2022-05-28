package vn.metamon.app.presentation.home.space

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import vn.metamon.data.model.Metamon

class SpaceCarouselPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val spaces: List<Metamon>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return spaces.size
    }

    override fun createFragment(position: Int): Fragment {
        return SpaceItemFragment.newInstance(spaces[position])
    }
}
