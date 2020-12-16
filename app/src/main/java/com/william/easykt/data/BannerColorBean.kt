package com.william.easykt.data

import android.graphics.Color
import androidx.annotation.ColorInt

/**
 * @author William
 * @date 2020/6/28 20:52
 * Class Comment：
 */
class BannerColorBean(
    @ColorInt var parseColor: Int = 0,
//    private val parseColor: Int = Color.parseColor(color),
    val alpha: Int = Color.alpha(parseColor),
    val red: Int = Color.red(parseColor),
    val green: Int = Color.green(parseColor),
    val blue: Int = Color.blue(parseColor)
)