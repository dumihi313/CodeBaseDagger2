package vn.metamon.app.presentation.main

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.main_tabs_layout.view.*
import vn.metamon.R
import vn.metamon.app.utils.getColorCompat
import vn.metamon.app.utils.inflate

private const val TAB_COUNT = 4

class MainTabLayout @JvmOverloads constructor(
    context: Context,
    attrsSet: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrsSet, defStyle) {

    private var tabs = arrayListOf<Tab>()

    private var selectedIndex: TabPosition = TabPosition.Home

    var selectionListener: TabSelectionListener? = null

    var tabClickListener: TabClickListener? = null

    init {
        isSaveEnabled = true
        inflate(R.layout.main_tabs_layout, true)
        setWillNotDraw(false)
        setBackgroundColor(context.getColorCompat(R.color.black_85))
        orientation = HORIZONTAL

        val selectedArray = resources.obtainTypedArray(R.array.MainTabSelectedDrawable)
        val unselectedArray = resources.obtainTypedArray(R.array.MainTabUnselectedDrawable)
        if (selectedArray.length() >= TAB_COUNT && unselectedArray.length() >= TAB_COUNT) {
            tabs.clear()
            val iconViews =
                arrayListOf(home_tab_icon, tab_icon_2, tab_icon_3, tab_icon_4)
            val textViews =
                arrayListOf(home_tab_text, b_tab_text, c_tab_text, d_tab_text)

            for (i in 0 until TAB_COUNT) {
                tabs.add(
                    Tab(
                        iconViews[i],
                        textViews[i],
                        Tab.Resource(selectedArray.getDrawable(i), unselectedArray.getDrawable(i)),
                        i == selectedIndex.index
                    )
                )
            }
        }
        selectedArray.recycle()
        unselectedArray.recycle()

        btn_tab_home.setOnClickListener {
            performClick(TabPosition.Home, it)
        }
        btn_tab_2.setOnClickListener {
            performClick(TabPosition.Tab2, it)
        }
        btn_tab_3.setOnClickListener {
            performClick(TabPosition.Tab3, it)
        }
        btn_tab_4.setOnClickListener {
            performClick(TabPosition.Tab4, it)
        }
    }

    private fun performClick(position: TabPosition, view: View) {
        tabClickListener?.onClick(position, view)
    }

    fun getCount() = TAB_COUNT

    fun setSelected(position: TabPosition) {
        if (selectedIndex == position) {
            selectionListener?.onReselected(selectedIndex)
        } else {
            tabs[selectedIndex.index].setSelected(false)
            selectedIndex = position
            tabs[selectedIndex.index].setSelected(true)
            selectionListener?.onSelected(selectedIndex)
        }
    }

    fun getSelected(): TabPosition = selectedIndex

    override fun onSaveInstanceState(): Parcelable? {
        return MainTabState(super.onSaveInstanceState(), selectedIndex)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val mainTabState = state as MainTabState
        super.onRestoreInstanceState(mainTabState.superState)

        setSelected(mainTabState.position)
    }

    class Tab(
        private val iconView: ImageView,
        private val tabText: TextView,
        private val resource: Resource,
        private var isSelected: Boolean
    ) {
        init {
            val drawable =
                if (isSelected) resource.iconSelectedDrawable else resource.iconUnselectedDrawable
            iconView.setImageDrawable(drawable)
        }

        fun setSelected(selection: Boolean) {
            if (selection != isSelected) {
                iconView.clearAnimation()
                val drawable =
                    if (selection) resource.iconSelectedDrawable else resource.iconUnselectedDrawable
                iconView.setImageDrawable(drawable)
                if (drawable is AnimationDrawable) {
                    drawable.start()
                }
                tabText.setTextColor(tabText.context.getColorCompat(if (selection) R.color.white else R.color.white_40))

                isSelected = selection
            }
        }

        class Resource(
            val iconSelectedDrawable: Drawable?,
            val iconUnselectedDrawable: Drawable?
        )
    }

    sealed class TabPosition(
        val index: Int
    ) {
        object Home : TabPosition(0)
        object Tab2 : TabPosition(1)
        object Tab3 : TabPosition(2)
        object Tab4 : TabPosition(3)

        companion object {
            fun from(index: Int): TabPosition {
                return when (index) {
                    1 -> Tab2
                    2 -> Tab3
                    3 -> Tab4
                    else -> Home
                }
            }
        }
    }

    interface TabSelectionListener {
        fun onSelected(position: TabPosition)

        fun onReselected(position: TabPosition)
    }

    fun interface TabClickListener {
        fun onClick(position: TabPosition, view: View)
    }

    private class MainTabState : BaseSavedState {
        val position: TabPosition

        constructor(superState: Parcelable?, position: TabPosition) : super(superState) {
            this.position = position
        }

        constructor(source: Parcel) : super(source) {
            position = TabPosition.from(source.readInt())
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeInt(position.index)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<MainTabState> {
            override fun createFromParcel(parcel: Parcel): MainTabState {
                return MainTabState(parcel)
            }

            override fun newArray(size: Int): Array<MainTabState?> {
                return arrayOfNulls(size)
            }
        }
    }
}