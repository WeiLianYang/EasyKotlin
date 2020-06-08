package com.william.base_component

import com.william.base_component.utils.getAppVersionName

/**
 * @author William
 * @date 2020/5/21 22:04
 * Class Comment：common params
 */
object Constants {

    var timeDiff: Long? = 0
    var deviceId: String? = ""
    var token: String? = ""
    var version: String = getAppVersionName()

}