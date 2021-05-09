package com.william.easykt.ui.scroll

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/**
 * author：William
 * date：4/23/21 10:39 PM
 * description：滚动LinearLayoutManager
 */
class ScrollingLinearLayoutManager(context: Context) : LinearLayoutManager(context) {

    /**
     * LinearLayoutManager滚动的速度，值越大滑动越慢
     */
    private var scrollSpeed = 30f

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView?,
        state: RecyclerView.State?,
        position: Int
    ) {
        recyclerView?.let {
            val linearSmoothScroller = object : LinearSmoothScroller(it.context) {
//                override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
//                    return this@ScrollingLinearLayoutManager.computeScrollVectorForPosition(
//                        targetPosition
//                    )
//                }

                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                    displayMetrics?.apply {
                        // 滑动1个像素所需的毫秒数
                        return scrollSpeed / displayMetrics.density
                    }
                    return super.calculateSpeedPerPixel(displayMetrics)
                }
            }

            linearSmoothScroller.targetPosition = position
            startSmoothScroll(linearSmoothScroller)
        }
    }
}