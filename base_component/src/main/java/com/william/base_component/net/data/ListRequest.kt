package com.william.base_component.net.data


/**
 * @author William
 * @date 2020/4/19 11:23
 * Class Comment：Paging request data model
 */
open class ListRequest(
    var page: Int = 0,// 页码
    var size: Int = 10// 页量
) {

    fun resetIndex() {
        page = 1
    }

    fun increaseIndex() {
        ++page
    }

    fun decreaseIndex() {
        --page
        if (page < 1) {
            page = 1
        }
    }
}