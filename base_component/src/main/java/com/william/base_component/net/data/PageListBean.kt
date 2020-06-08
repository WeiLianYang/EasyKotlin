package com.william.base_component.net.data

/**
 * @author William
 * @date 2020/4/24 22:22
 * Class Comment：Paging response data model
 */
open class PageListBean<T> {
    var total = 0 // 返回的数据总数
    var page = 0 // 当前页码
    var size = 0 // 当前页码返回的数据数
    var list: MutableList<T>? = null
}