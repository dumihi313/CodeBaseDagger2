package vn.metamon.app.presentation.home.space

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_home_space.*
import vn.metamon.R
import vn.metamon.app.utils.observeEvent
import vn.metamon.app.widget.utils.gone
import vn.metamon.app.widget.utils.visible
import vn.metamon.data.model.Metamon
import vn.metamon.data.model.Space
import javax.inject.Inject
import kotlin.math.abs
import kotlin.collections.isNotEmpty

class SpaceFragment : DaggerFragment() {
    companion object {
        private const val SCALE_RATIO = 0.55f

        fun newInstance(): SpaceFragment {
            val fragment = SpaceFragment()
            fragment.arguments = bundleOf()
            return fragment
        }
    }

    @Inject
    lateinit var appContext: Context

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val spaceViewModel by viewModels<SpaceViewModel> { viewModelFactory }

    private val metamonViewModel by viewModels<MetamonViewModel> { viewModelFactory }

    private val spaceSharedViewModel by viewModels<SpaceShareViewModel>(ownerProducer = { this })

    private lateinit var spaceCarouselPagerAdapter: SpaceCarouselPagerAdapter

    init {
        lifecycleScope.launchWhenCreated {
            spaceViewModel.getUniSpace()
            metamonViewModel.getKingdom()
            metamonViewModel.getMetamons(1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_space, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initLiveData()

        with(spaceList) {
            offscreenPageLimit = 1

            val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
            val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
            val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
                page.translationX = -(page.width/2 * (1- SCALE_RATIO) + currentItemHorizontalMarginPx + nextItemVisiblePx) * position
                page.scaleY = 1 - (SCALE_RATIO * abs(position))
                page.scaleX = 1 - (SCALE_RATIO * abs(position))
            }
            setPageTransformer(pageTransformer)
            addItemDecoration(SpaceItemDecoration(appContext))

            registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {

                private var currentPosition = 1

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentPosition = position
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    val dotsCount = spaceCarouselPagerAdapter.itemCount
                    if (state == ViewPager2.SCROLL_STATE_IDLE || state == ViewPager2.SCROLL_STATE_DRAGGING) {
                        if (currentPosition == 0)
                            this@with.setCurrentItem(dotsCount - 2, false)
                        else if (currentPosition == dotsCount - 1)
                            this@with.setCurrentItem(1, false)
                    }
                }
            })
        }

        refreshLayout.setOnRefreshListener {
            spaceViewModel.getUniSpace()
        }
    }

    private fun initLiveData() {
//        spaceViewModel.uniSpaceList.observeEvent(viewLifecycleOwner) {
//            if (it.isNotEmpty()) {
//                spaceViewModel.getSpace(it[0].id)
//            }
//        }

        metamonViewModel.metamonList.observeEvent(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                spaceList.visible()
                emptyText.gone()

                val infiniteData = transformDataToInfinity2(it)
                spaceCarouselPagerAdapter = SpaceCarouselPagerAdapter(childFragmentManager, lifecycle, infiniteData)

                spaceList.adapter = spaceCarouselPagerAdapter
                spaceList.setCurrentItem(1, false)
            } else {
                spaceList.gone()
                emptyText.visible()
            }
        }

        spaceViewModel.spaceList.observeEvent(viewLifecycleOwner) {
//            if (it.isNotEmpty()) {
//                spaceList.visible()
//                emptyText.gone()
//
//                val infiniteData = transformDataToInfinity(it)
//                spaceCarouselPagerAdapter = SpaceCarouselPagerAdapter(childFragmentManager, lifecycle, infiniteData)
//
//                spaceList.adapter = spaceCarouselPagerAdapter
//                spaceList.setCurrentItem(1, false)
//            } else {
//                spaceList.gone()
//                emptyText.visible()
//            }
        }

        spaceViewModel.isLoading.observeEvent(viewLifecycleOwner) {
            refreshLayout.isRefreshing = it
        }

        spaceViewModel.error.observeEvent(viewLifecycleOwner) {
            Toast.makeText(appContext, "Có lỗi xảy ra khi xử lý!", Toast.LENGTH_SHORT).show()
        }

        spaceSharedViewModel.spaceClick.observeEvent(viewLifecycleOwner) {
            //todo: handle space click
            Toast.makeText(appContext, it.name, Toast.LENGTH_SHORT).show()
        }
    }

    private fun transformDataToInfinity2(data: List<Metamon>): List<Metamon> {
        if (data.size > 1) {
            val transformData = arrayListOf(data.last())
            transformData.addAll(data)
            transformData.add(data.first())
            return transformData
        }
        return data
    }

    private fun transformDataToInfinity(data: List<Space>): List<Space> {
        if (data.size > 1) {
            val transformData = arrayListOf(data.last())
            transformData.addAll(data)
            transformData.add(data.first())
            return transformData
        }
        return data
    }
}