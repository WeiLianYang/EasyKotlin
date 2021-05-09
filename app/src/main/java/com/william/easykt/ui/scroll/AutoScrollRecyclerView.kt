package com.william.easykt.ui.scroll

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 * author：William
 * date：4/23/21 10:39 PM
 * description：支持自动滚动，但无法手势滑动的 RecyclerView
 */
class AutoScrollRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return false
    }
}