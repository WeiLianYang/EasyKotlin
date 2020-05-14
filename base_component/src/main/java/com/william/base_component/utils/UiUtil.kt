package com.william.base_component.utils

import com.william.base_component.BaseApp


/**
 * @author William
 * @date 2020/4/16 17:23
 * Class Comment：
 */

fun getDensity(): Float = BaseApp.instance.mContext.resources.displayMetrics.density

fun getFloatPixel(dp: Int): Float = dp * getDensity()

fun getIntPixel(dp: Int): Int = getFloatPixel(dp).toInt()

fun getScreenWidth(): Int = BaseApp.instance.mContext.resources.displayMetrics.widthPixels

fun getScreenHeight(): Int = BaseApp.instance.mContext.resources.displayMetrics.heightPixels
