package vn.metamon.app.presentation.home.space

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import vn.metamon.app.utils.dp

class SpaceUniverseDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val leftSpacing = context.dp(20f)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = leftSpacing
    }
}