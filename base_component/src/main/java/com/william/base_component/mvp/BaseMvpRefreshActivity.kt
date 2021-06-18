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

package com.william.base_component.mvp

import androidx.viewbinding.ViewBinding
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.william.base_component.databinding.BaseActivityRefreshBinding
import com.william.base_component.mvp.base.BasePresenter
import com.william.base_component.mvp.base.IBaseModel
import com.william.base_component.mvp.base.IBaseRefreshView
import com.william.base_component.mvp.base.IBaseView
import com.william.base_component.widgets.CustomClassicsFooter


/**
 * @author William
 * @date 2020/4/22 20:09
 * Class Comment：可以用于下拉刷新和上拉加载通用父类(也可以纯滚动)，可包裹RecyclerView, ListView, WebView或者一组控件，可设置嵌套滑动
 * 更多内容见 https://github.com/scwang90/SmartRefreshLayout
 * 常见问题见 https://github.com/scwang90/SmartRefreshLayout/blob/master/art/md_faq.md
 *
 * 子类有两种继承方案：
 *    第一种：实现mContentViewBinding，重写getContentLayoutId()
 *         （适用于子类通用简单的布局情况）
 *
 *    第二种：重写getLayoutId()，实现mViewBinding，类型对应重写的getLayoutId()的布局，
 *          重写getSmartRefreshLayout()，其返回值由重写的mViewBinding提供
 *          实现mContentViewBinding，重写getContentLayoutId()
 *         （适用于页面上有不需要随着页面滚动而滚动的布局，例如一个悬浮按钮或者是一个顶部或底部控件）
 */
abstract class BaseMvpRefreshActivity<P : BasePresenter<out IBaseView, out IBaseModel>> :
    BaseMvpActivity<P>(), IBaseRefreshView {

    override val mViewBinding: ViewBinding by lazy(LazyThreadSafetyMode.NONE) {
        BaseActivityRefreshBinding.inflate(layoutInflater)
    }

    /**
     * 子类用标准委托的方式实现这个属性
     */
    protected abstract val mContentViewBinding: ViewBinding

    override fun initView() {
        super.initView()
        initRefreshSetting()
    }

    open fun getSmartRefreshLayout(): SmartRefreshLayout? =
        (mViewBinding as? BaseActivityRefreshBinding)?.mSmartRefreshLayout

    private fun initRefreshSetting() {
        getSmartRefreshLayout()?.apply {
            //是否下拉Header的时候向下平移列表或者内容
            setEnableHeaderTranslationContent(false)
            //是否上拉Footer的时候向上平移列表或者内容
            setEnableFooterTranslationContent(true)
            setEnableRefresh(true)
            setEnableLoadMore(false)

            // 设置子布局
            setRefreshContent(mContentViewBinding.root)
            setRefreshHeader(MaterialHeader(mActivity))
            val footer = CustomClassicsFooter(mActivity)
            footer.setFinishDuration(0)
            setRefreshFooter(footer)
            //设置是否在没有更多数据之后 Footer 跟随内容
            setEnableFooterFollowWhenNoMoreData(true)
            setEnableNestedScroll(false)
            setOnRefreshListener { refreshLayout: RefreshLayout? ->
                onRefresh(refreshLayout)
            }
            setOnLoadMoreListener { refreshLayout: RefreshLayout? ->
                onLoadMore(refreshLayout)
            }

            setEnablePureScrollMode(isPureScroll())
        }
    }

    /**
     * 是否是纯滚动模式
     */
    open fun isPureScroll(): Boolean = false

    abstract fun onRefresh(refreshLayout: RefreshLayout?)

    abstract fun onLoadMore(refreshLayout: RefreshLayout?)

    fun setEnableRefresh(enableRefresh: Boolean) {
        getSmartRefreshLayout()?.setEnableRefresh(enableRefresh)
    }

    fun setEnableLoadMore(enableLoadMore: Boolean) {
        getSmartRefreshLayout()?.setEnableLoadMore(enableLoadMore)
    }

    fun finishRefresh(success: Boolean = true) {
        getSmartRefreshLayout()?.finishRefresh(success)
    }

    fun finishLoadMore(success: Boolean = true) {
        getSmartRefreshLayout()?.finishLoadMore(success)
    }

    fun finishLoadMoreWithNoMoreData() {
        getSmartRefreshLayout()?.finishLoadMoreWithNoMoreData()
    }

    fun finishRefreshAndLoadMore() {
        getSmartRefreshLayout()?.closeHeaderOrFooter()
    }

    fun setNoMoreData(noMoreData: Boolean = true) {
        getSmartRefreshLayout()?.setNoMoreData(noMoreData)
    }

    fun autoRefresh() {
        getSmartRefreshLayout()?.autoRefresh()
    }

    fun autoLoadMore() {
        getSmartRefreshLayout()?.autoLoadMore()
    }

    fun setHeaderInsetStart(insetDp: Float) {
        getSmartRefreshLayout()?.setHeaderInsetStart(insetDp)
    }

    override fun setupRefreshStatus(success: Boolean) {
        if (success) {
            finishRefresh()
            // 恢复状态
            setNoMoreData(false)
        } else {
            // 刷新失败（不会更新时间）
            finishRefresh(false)
        }
    }

    override fun setupLoadMoreStatus(success: Boolean, hasMoreData: Boolean) {
        if (success) {
            if (hasMoreData) {
                finishLoadMore()
            } else {
                // 显示全部加载完成，并不再触发加载更事件
                finishLoadMoreWithNoMoreData()
            }
        } else {
            // 加载失败
            finishLoadMore(false)
        }
    }

    override fun onStop() {
        finishRefresh()
        super.onStop()
    }

}