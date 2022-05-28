package vn.metamon.app.presentation.home.space

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import vn.metamon.R

class SpaceItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val horizontalMarginInPx =
        context.resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin).toInt()

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.right = horizontalMarginInPx
        outRect.left = horizontalMarginInPx
    }

}