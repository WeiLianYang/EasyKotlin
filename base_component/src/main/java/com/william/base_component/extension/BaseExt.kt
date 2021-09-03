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

package com.william.base_component.extension

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.william.base_component.BaseApp

/**
 * @author William
 * @date 2020/4/18 20:52
 * Class Comment：Custom extension functions and extension properties.
 */
fun String?.toast(duration: Int = Toast.LENGTH_SHORT) {
    this?.let {
        Toast.makeText(BaseApp.instance, it, duration).show()
    }
}

fun Int.toast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(BaseApp.instance, this, duration).show()
}

fun String?.snackbar(
    view: View,
    actionText: String? = null,
    duration: Int = Snackbar.LENGTH_SHORT,
    listener: (v: View) -> Unit = {}
) {
    this?.let { text ->
        val bar = Snackbar.make(view, text, duration)
        actionText?.let { bar.setAction(it, listener) }
        bar.show()
    }
}

fun Int.snackbar(
    view: View,
    actionTextId: Int? = null,
    duration: Int = Snackbar.LENGTH_SHORT,
    listener: (v: View) -> Unit = {}
) {
    val bar = Snackbar.make(view, this, duration)
    actionTextId?.let { bar.setAction(it, listener) }
    bar.show()
}

val Int.dp
    get() = this.toFloat().dp.toInt()

val Float.dp
    get() = getTypedValue(TypedValue.COMPLEX_UNIT_DIP, this)

val Float.sp
    get() = getTypedValue(TypedValue.COMPLEX_UNIT_SP, this)

private fun getTypedValue(unit: Int, value: Float): Float {
    return TypedValue.applyDimension(unit, value, Resources.getSystem().displayMetrics)
}
