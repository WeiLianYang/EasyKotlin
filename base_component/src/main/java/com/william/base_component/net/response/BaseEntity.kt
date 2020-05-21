package com.william.base_component.net.response


/**
 * @author William
 * @date 2020/5/19 11:18
 * Class Comment：BaseEntity
 */
open class BaseEntity(
    var errorCode: Int? = 0,// 返回码
    var errorMsg: String? = null// 信息
)