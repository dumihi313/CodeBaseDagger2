package vn.metamon.app.presentation.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.main_tabs_layout.*
import vn.metamon.app.di.model.BackPressedListener
import vn.metamon.app.presentation.main.MainTabLayout.TabPosition.*
import vn.metamon.R
import vn.metamon.app.Constants
import vn.metamon.app.utils.LoginPreActionListener
import vn.metamon.data.MetamonUserManager
import javax.inject.Inject

class MainFragment : DaggerFragment(), BackPressedListener {
    companion object {
        const val TAG = "MainFragment"

        fun newInstance(): MainFragment {
            val args = Bundle()

            val fragment = MainFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var appContext: Context

    @Inject
    lateinit var userManager: MetamonUserManager

    private lateinit var loginPreActionListener: LoginPreActionListener

    private var lastBackPressedTs = 0L

    private lateinit var pagerAdapter: MainPagerAdapter

    private lateinit var tabLayout: MainTabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginPreActionListener = LoginPreActionListener(
            userManager,
            this
        )

        tabLayout = view.findViewById(R.id.tabLayout)
        pagerAdapter = MainPagerAdapter(childFragmentManager, lifecycle, tabLayout.getCount())

        mainPager.adapter = pagerAdapter
        mainPager.currentItem = tabLayout.getSelected().index
        mainPager.isUserInputEnabled = false
        mainPager.offscreenPageLimit = pagerAdapter.itemCount

        mainPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val tabPosition = MainTabLayout.TabPosition.from(position)
                tabLayout.setSelected(tabPosition)
            }
        })

        tabLayout.tabClickListener = MainTabLayout.TabClickListener { position, v ->
            when (position) {
                Home -> {
                    mainPager.currentItem = position.index
                }
                Tab2 -> {
                    mainPager.currentItem = position.index
                }
                Tab3 -> {
//                    loginPreActionListener.onClick(v) {
//
//                    }
                    mainPager.currentItem = position.index
                }
                Tab4 -> {
                    mainPager.currentItem = position.index
                }

            }
        }

        btn_add.setOnClickListener { view ->
            loginPreActionListener.onClick(view) {
                Toast.makeText(context, "Logged in- Happy Coding", Toast.LENGTH_SHORT).show()
//                findNavHost()?.let { navHost ->
//                    val fm = navHost.navHostFragmentManager()
//                    fm.beginTransaction().run {
//                        navHost.containerView().setBackgroundColor(Color.BLACK)
//                        fm.findFragmentById(navHost.containerId())?.let { hide(it) }
//                        setCustomAnimations(
//                            R.anim.slide_in_up,
//                            R.anim.slide_out_down,
//                            R.anim.slide_in_down,
//                            R.anim.slide_out_up
//                        )
//                        add(navHost.containerId(), CreateContentFragment.newInstance())
//                        addToBackStack(CreateContentFragment.TAG)
//                        commit()
//                    }
//                }
            }
        }
    }

    override fun onBackPressed(): Boolean {
        val currentMillis = System.currentTimeMillis()
        return if (currentMillis - lastBackPressedTs <= Constants.BACK_PRESSED_INTERVAL) {
            false
        } else {
            Toast.makeText(
                appContext,
                appContext.getString(R.string.pressed_back_to_exit),
                Toast.LENGTH_SHORT
            )
                .show()
            lastBackPressedTs = currentMillis
            true
        }
    }
}