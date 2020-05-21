package com.william.base_component.net.request


/**
 * @author William
 * @date 2020/5/19 11:23
 * Class Comment：Paging request data model
 */
data class ListReq(
    var page: Int? = 1,// 页码
    var size: Int? = 10// 页量
)