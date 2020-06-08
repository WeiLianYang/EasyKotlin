package com.william.base_component.mvp.base


/**
 * @author William
 * @date 2020/5/17 20:09
 * Class Comment：
 */
interface IBaseRefreshView : IBaseView {

    /**
     * 设置刷新后的状态
     * @param success 刷新结果
     */
    fun setupRefreshStatus(success: Boolean)

    /**
     * 设置加载更多后的状态
     * @param success 刷新结果
     * @param hasMoreData 有更多可以加载的数据，默认为true
     */
    fun setupLoadMoreStatus(success: Boolean, hasMoreData: Boolean = true)

}