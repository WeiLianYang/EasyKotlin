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

package com.william.easykt.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import com.william.easykt.R

/**
 * author：William
 * date：2022/5/8 20:53
 * description：侧滑布局
 */
class SideSlipLayout : FrameLayout {

    /** 最大位移距离 */
    private var maxSlideOffset = 500

    /** 菜单视图 */
    private var sideView: View? = null

    /** 内容视图 */
    private var contentView: View? = null

    /** 拖拽帮助类 */
    private var viewDragHelper: ViewDragHelper? = null

    constructor(context: Context) : super(context, null) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    private fun initView() {
        if (viewDragHelper == null) {
            /**
             * forParent 需要监听的View
             * callback 拖拽的回调
             */
            viewDragHelper = ViewDragHelper.create(this, object : ViewDragHelper.Callback() {

                /**
                 * 用于决定需要拦截 ViewGroup 中的哪个控件的触摸事件
                 *
                 * @param child     当前用户触摸的子控件的 View 对象
                 * @param pointerId 当前触摸此控件的手指所对应的 pointerId
                 *
                 * @return true 对这个 View 进行各种事件的捕捉，否则 不会捕捉
                 */
                override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                    // 捕捉内容视图的触摸事件
                    return child == contentView
                }

                /**
                 * 当手指在子 View 上横向移动时，会回调该方法。
                 *
                 * @param child 当前手指横向移动所在的子 View
                 * @param left  当前子 View 如果跟随手指移动，即为它即将移动到新的 left 坐标值。
                 * @param dx    手指横向移动的距离
                 *
                 * @return 返回子 View 新的 left 坐标值，程序会将该子 View 移动到该位置
                 */
                override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                    return if (left > 0) {
                        left.coerceAtMost(maxSlideOffset)
                    } else 0
                }

                /**
                 * 当子视图不再被主动拖动时调用。如果相关，还提供抛掷速度。速度值可以被限制在系统最小值或最大值。
                 * 调用代码可能会决定抛出或以其他方式释放视图以使其固定到位。它应该使用 {@link setCapturedViewAt(int, int)} 或 {@link flingCapturedView(int, int, int, int)} 来完成。
                 * 如果回调调用这些方法之一，ViewDragHelper 将进入 {@link STATE_SETTLING} 并且视图捕获将不会完全结束，直到它完全停止。
                 * 如果在 onViewReleased 返回之前没有调用这些方法，则视图将停止在原地，并且 ViewDragHelper 将返回到 STATE_IDLE。
                 *
                 * @param releasedChild 被拖拽即将被释放的子 View
                 * @param xvel 指针离开屏幕时的 X 速度（以每秒像素为单位）。
                 * @param yvel 指针离开屏幕时的 Y 速度，以每秒像素为单位。
                 */
                override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                    super.onViewReleased(releasedChild, xvel, yvel)
                    // 手指抬起后缓慢移动到指定位置
                    if (contentView!!.left < maxSlideOffset / 2) {
                        // 关闭菜单，将视图子动画设置到给定的（左，上）位置
                        viewDragHelper?.smoothSlideViewTo(contentView!!, 0, 0)
                    } else {
                        // 打开菜单
                        viewDragHelper?.smoothSlideViewTo(contentView!!, maxSlideOffset, 0)
                    }
                    invalidate()
                }

                /**
                 * 当捕获的视图的位置由于拖动或固定而发生变化时调用。
                 *
                 * @param changedView View whose position changed
                 * @param left New X coordinate of the left edge of the view
                 * @param top New Y coordinate of the top edge of the view
                 * @param dx Change in X position from the last call
                 * @param dy Change in Y position from the last call
                 */
                override fun onViewPositionChanged(
                    changedView: View, left: Int, top: Int, dx: Int, dy: Int
                ) {
                    super.onViewPositionChanged(changedView, left, top, dx, dy)
                    // 位置变化百分比
                    val percent = contentView!!.left / maxSlideOffset.toFloat()
                    executeAnimation(percent)
                }
            })
        }
    }

    /**
     * 执行动画
     * @param percent 百分比
     */
    private fun executeAnimation(percent: Float) {
        sideView?.scaleX = 0.8f + 0.2f * percent
        sideView?.scaleY = 0.8f + 0.2f * percent
        sideView?.translationX = -maxSlideOffset / 2f + maxSlideOffset / 2 * percent

        contentView?.scaleX = 1 - percent * 0.2f
        contentView?.scaleY = 1 - percent * 0.2f
    }

    /**
     * 重写事件拦截方法，将事件传给 ViewDragHelper 进行处理
     * @param ev event
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return viewDragHelper?.shouldInterceptTouchEvent(ev) ?: super.onInterceptTouchEvent(ev)
    }

    /**
     * 将触摸事件传递给 ViewDragHelper
     * @param ev event
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        viewDragHelper?.processTouchEvent(ev)
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (viewDragHelper?.continueSettling(true) == true) {
            invalidate()
        }
    }

    /**
     * 设置内容视图和菜单视图
     * @param sideView 菜单视图
     * @param sideLayoutParams 菜单布局参数
     * @param contentView 内容视图
     * @param contentLayoutParams 内容布局参数
     */
    fun setSideContentView(
        sideView: View, contentView: View,
        sideLayoutParams: LayoutParams? = null, contentLayoutParams: LayoutParams? = null
    ) {
        this.sideView = sideView
        this.contentView = contentView

        if (sideLayoutParams != null) {
            addView(sideView, sideLayoutParams)
            maxSlideOffset = sideLayoutParams.width
        } else {
            addView(sideView)
        }

        if (contentLayoutParams != null) {
            addView(contentView, contentLayoutParams)
        } else {
            addView(contentView)
        }
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        super.addView(child, index, params)

        if (sideView == null) {
            sideView = findViewById(R.id.slide_side)
        }
        if (contentView == null) {
            contentView = findViewById(R.id.slide_content)
        }
    }

    /** 关闭菜单 */
    fun closeSideView() {
        viewDragHelper?.smoothSlideViewTo(contentView!!, 0, 0)
        ViewCompat.postInvalidateOnAnimation(this)
    }
}