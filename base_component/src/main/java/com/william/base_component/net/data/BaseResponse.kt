package com.william.base_component.net.data


/**
 * @author William
 * @date 2020/4/19 11:18
 * Class Comment：BaseResponse
 */
open class BaseResponse<T>(
    var errorCode: Int? = 0,// 业务码
    var errorMsg: String? = null,// 异常信息
    val data: T// 业务数据
)