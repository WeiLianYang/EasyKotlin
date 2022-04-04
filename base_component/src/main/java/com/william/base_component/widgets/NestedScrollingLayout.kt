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

package com.william.base_component.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingParent
import androidx.core.view.ViewCompat
import com.william.base_component.R
import com.william.base_component.extension.logD
import kotlin.math.abs

/**
 * author : WilliamYang
 * date : 2021/7/30 10:00
 * description : 嵌套滑动父布局
 */
class NestedScrollingLayout : FrameLayout, NestedScrollingParent {
    /**
     * 是否是往下拉
     */
    private var scrollDown = false

    /**
     * 是否是往上滑
     */
    private var scrollUp = false

    /**
     * 外部View滑动的最大距离
     */
    private var mTopViewHeight = 0

    constructor(context: Context, attrs: AttributeSet?) : super(
        context, attrs
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    /**
     * 内部View通知外部View准备开始滑动：ACTION_DOWN 事件，对应 [NestedScrollingChild] startNestedScroll()
     */
    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        "onStartNestedScroll".logD(TAG)
        return nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    /**
     * 内部View通知外部View准备开始滑动：ACTION_DOWN 事件，对应 [NestedScrollingChild] startNestedScroll()
     */
    override fun onNestedScrollAccepted(child: View, target: View, nestedScrollAxes: Int) {
        "onNestedScrollAccepted".logD(TAG)
    }

    /**
     * 内部View在滑动之前通知外部View是否要处理这次滑动的横向和纵向距离，外部View处理之后，剩下的才交给内部View处理。
     * ACTION_MOVE 事件，对应 [NestedScrollingChild] dispatchNestedPreScroll()
     *
     * dx和dy参数表示滑动的横向和纵向距离，consumed参数表示消耗的横向和纵向距离，
     * 如纵向滚动，只需要消耗了dy/2，表示父View和内部View分别处理这次滚动距离的 1/2
     *
     * @param target   内部View
     * @param dx       滑动的横向距离
     * @param dy       滑动的纵向距离
     * @param consumed 外部View消耗的横向和纵向距离
     */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        "dy: $dy, consumed: ${consumed.contentToString()}".logD(TAG)
        var deltaY = dy
        scrollDown =
            deltaY < 0 && abs(scrollY) < mTopViewHeight && !target.canScrollVertically(-1)
        if (scrollDown) {
            if (abs(scrollY + deltaY) > mTopViewHeight) {
                "向下滑动，超过了最大高度".logD(TAG)
                deltaY = -(mTopViewHeight - abs(scrollY))
            }
        }
        scrollUp = deltaY > 0 && scrollY < 0
        if (scrollUp) {
            if (deltaY + scrollY > 0) {
                "向上滑动，超过了初始位置".logD(TAG)
                deltaY = -scrollY
            }
        }
        "scrollDown: $scrollDown, scrollUp: $scrollUp, deltaY: $deltaY, scrollY: $scrollY".logD(TAG)
        if (scrollDown || scrollUp) {
            consumed[1] = deltaY // 父View消耗滑动的纵向距离
            scrollBy(0, deltaY) // 父View滚动
        }
    }

    /**
     * 内部View在滑动的时候，通知外部View是否也要跟随处理这次滑动，各自处理各自的
     *
     * ACTION_MOVE 事件后已经开始滑动 对应 [NestedScrollingChild] dispatchNestedScroll()
     */
    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        "onNestedScroll".logD(TAG)
    }

    /**
     * 内部View在Fling操作之前通知外部View是否要处理这次Fling操作，
     * 如果外部View处理了则内部View不再处理，如果外部View没有处理则内部View处理。
     *
     * fling 对应 [NestedScrollingChild] dispatchNestedPreFling()
     */
    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        "onNestedPreFling".logD(TAG)
        return scrollY != 0
    }

    /**
     * 内部View在Fling操作时通知外部View是否要处理这次Fling操作，
     * 如果外部View处理了则内部View不再处理，如果外部View没有处理则内部View处理。
     *
     * fling 对应 [NestedScrollingChild] dispatchNestedFling()
     */
    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        "onNestedFling".logD(TAG)
        return scrollY != 0
    }

    /**
     * 内部View通知外部View停止滑动：ACTION_UP 事件，对应 [NestedScrollingChild] stopNestedScroll()
     */
    override fun onStopNestedScroll(target: View) {
        "onStopNestedScroll".logD(TAG)
    }

    override fun getNestedScrollAxes(): Int {
        return ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mTopViewHeight = resources.getDimensionPixelSize(R.dimen.dp_240)
    }

    companion object {
        private const val TAG = "NestedParent"
    }
}