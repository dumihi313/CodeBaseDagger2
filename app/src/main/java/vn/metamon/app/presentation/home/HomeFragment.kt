package vn.metamon.app.presentation.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_home.*
import vn.metamon.R
import vn.metamon.app.utils.getColorCompat
import javax.inject.Inject

class HomeFragment : DaggerFragment() {
    companion object {
        const val TAG = "HomeFragment"

        private const val SELECTED_TEXT_SIZE = 34f
        private const val UNSELECTED_TEXT_SIZE = 22f

        fun newInstance() = HomeFragment()
            .apply {}
    }

    @Inject
    lateinit var appContext: Context

    private lateinit var adapter: HomePagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = HomePagerAdapter(this)
        homePager.adapter = adapter
        homePager.isUserInputEnabled = false

        TabLayoutMediator(homeTabLayout, homePager) { tab, position ->
            val layout = layoutInflater.inflate(R.layout.layout_home_tab_text, null)
            val tabText = layout.findViewById<TextView>(R.id.tabText)
            tabText.text = adapter.getPageTitle(position)
            if (position == 0) {
                tabText.typeface = ResourcesCompat.getFont(appContext, R.font.sf_pro_text_heavy)
                tabText.setTextColor(Color.WHITE)
                tabText.textSize = SELECTED_TEXT_SIZE
            }
            tab.customView = layout
        }.attach()

        homeTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                setTabSelected(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                setTabUnSelected(tab)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setTabSelected(tabLayout: TabLayout.Tab?) {
        tabLayout?.customView?.let {
            val tabText = it.findViewById<TextView>(R.id.tabText)
            tabText.typeface = ResourcesCompat.getFont(appContext, R.font.sf_pro_text_heavy)
            tabText.setTextColor(Color.WHITE)
            tabText.textSize = SELECTED_TEXT_SIZE
        }
    }

    private fun setTabUnSelected(tabLayout: TabLayout.Tab?) {
        tabLayout?.customView?.let {
            val tabText = it.findViewById<TextView>(R.id.tabText)
            tabText.typeface = ResourcesCompat.getFont(appContext, R.font.sf_pro_text_regular)
            tabText.setTextColor(appContext.getColorCompat(R.color.white_50))
            tabText.textSize = UNSELECTED_TEXT_SIZE
        }
    }

}