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