/*
 * Copyright WeiLianYang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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