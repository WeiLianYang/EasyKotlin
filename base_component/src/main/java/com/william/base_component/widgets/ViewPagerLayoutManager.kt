package com.william.base_component.widgets

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.orhanobut.logger.Logger

/**
 * @author WilliamYang
 * @date 2021/2/21 17:27
 * Class Comment：配合RecyclerView实现ViewPager的切换效果
 */
class ViewPagerLayoutManager : LinearLayoutManager {
    private var mPagerSnapHelper: PagerSnapHelper? = null
    private var mOnPagerChangeListener: OnPagerChangeListener? = null
    private var offset = 0 //位移，用来判断移动方向
    var checkChildCount = 1// 检测数量

    constructor(context: Context?, orientation: Int) : super(context, orientation, false) {
        init()
    }

    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    ) {
        init()
    }

    private fun init() {
        mPagerSnapHelper = PagerSnapHelper()
    }

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        mPagerSnapHelper?.attachToRecyclerView(view)
        view.addOnChildAttachStateChangeListener(mChildAttachStateChangeListener)
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
    }

    override fun onScrollStateChanged(state: Int) {
        when (state) {
            RecyclerView.SCROLL_STATE_IDLE -> {
                // 空闲状态
                mPagerSnapHelper?.findSnapView(this)?.let {
                    val position = getPosition(it)
                    if (childCount == checkChildCount) {
                        mOnPagerChangeListener?.onPageSelected(position, position == itemCount - 1)
                    }
                }
            }
            RecyclerView.SCROLL_STATE_DRAGGING -> {
                // 缓慢拖拽
            }
            RecyclerView.SCROLL_STATE_SETTLING -> {
                // 快速滚动
            }
        }
    }

    /**
     * 监听竖直方向的相对偏移量
     */
    override fun scrollVerticallyBy(dy: Int, recycler: Recycler, state: RecyclerView.State): Int {
        offset = dy
        return super.scrollVerticallyBy(dy, recycler, state)
    }

    /**
     * 监听水平方向的相对偏移量
     */
    override fun scrollHorizontallyBy(dx: Int, recycler: Recycler, state: RecyclerView.State): Int {
        offset = dx
        return super.scrollHorizontallyBy(dx, recycler, state)
    }

    /**
     * 设置监听
     */
    fun setOnPagerChangeListener(listener: OnPagerChangeListener?) {
        mOnPagerChangeListener = listener
    }

    private val mChildAttachStateChangeListener: OnChildAttachStateChangeListener =
        object : OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {
                if (childCount == checkChildCount) {
                    mOnPagerChangeListener?.onInitComplete()
                }
            }

            override fun onChildViewDetachedFromWindow(view: View) {
                mOnPagerChangeListener?.onPageRelease(offset >= 0, getPosition(view))
            }
        }

    open class SimpleOnPagerChangeListener : OnPagerChangeListener {
        override fun onInitComplete() {
            Logger.d("onInitComplete")
        }

        override fun onPageRelease(isNext: Boolean, position: Int) {
            Logger.d("isNext:$isNext, position:$position")
        }

        override fun onPageSelected(position: Int, isBottom: Boolean) {
            Logger.d("position:$position, isBottom:$isBottom")
        }
    }

    interface OnPagerChangeListener {
        /**
         * 初始化完成
         */
        fun onInitComplete()

        /**
         * 释放的监听
         */
        fun onPageRelease(isNext: Boolean, position: Int)

        /**
         * 选中的监听以及判断是否滑动到底部
         */
        fun onPageSelected(position: Int, isBottom: Boolean)
    }
}

