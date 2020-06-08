package com.william.base_component.utils

import android.content.res.Resources
import com.william.base_component.BaseApp


/**
 * @author William
 * @date 2020/4/16 17:23
 * Class Comment：
 */

fun getDensity(): Float = BaseApp.instance.resources.displayMetrics.density

fun getScreenWidth(): Int = BaseApp.instance.resources.displayMetrics.widthPixels

fun getScreenHeight(): Int = BaseApp.instance.resources.displayMetrics.heightPixels

fun getStatusBarHeight(): Int {
    val resources = Resources.getSystem()
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(resourceId)
}

fun getNavBarHeight(): Int {
    val res = Resources.getSystem()
    val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId != 0) {
        res.getDimensionPixelSize(resourceId)
    } else {
        0
    }
}