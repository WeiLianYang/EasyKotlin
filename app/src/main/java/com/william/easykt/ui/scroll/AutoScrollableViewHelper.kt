package com.william.easykt.ui.scroll

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.william.base_component.utils.getScreenHeight
import com.william.easykt.R
import com.william.easykt.ui.adapter.ScrollImageAdapter

/**
 * author：William
 * date：4/23/21 10:39 PM
 * description：添加可滑动的视图帮助类
 */

fun addScrollImageView(viewGroup: ViewGroup?) {
    viewGroup?.addView(createScrollImageView(viewGroup.context))
}

@SuppressLint("InflateParams")
fun createScrollImageView(context: Context, height: Int = 0): ViewGroup {
    val recyclerView = AutoScrollRecyclerView(context).apply {
        layoutManager = ScrollingLinearLayoutManager(context)
        adapter = ScrollImageAdapter()
        smoothScrollToPosition(Int.MAX_VALUE / 2)
    }
    val layerView = View(context)
    layerView.background =
        ContextCompat.getDrawable(context, R.drawable.gradient_layer)
    return FrameLayout(context).apply {
        val paramsHeight = if (height == 0) getScreenHeight() else height
        layoutParams =
            FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, paramsHeight)
        addView(recyclerView)
        addView(layerView)
    }
}
