package vn.metamon.app.presentation.home.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import dagger.android.support.DaggerFragment
import vn.metamon.R

class FollowingFragment: DaggerFragment() {
    companion object {
        fun newInstance(): FollowingFragment {
            val fragment = FollowingFragment()
            fragment.arguments = bundleOf()
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_following, container, false)
    }
}