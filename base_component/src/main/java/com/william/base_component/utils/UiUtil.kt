/*
 * Copyright WeiLianYang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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