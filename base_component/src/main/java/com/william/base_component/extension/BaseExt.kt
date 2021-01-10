package com.william.base_component.extension

import android.content.res.Resources
import android.util.TypedValue
import android.widget.Toast
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


val Int.dp
    get() = this.toFloat().dp.toInt()

val Float.dp
    get() = getTypedValue(TypedValue.COMPLEX_UNIT_DIP, this)

val Float.sp
    get() = getTypedValue(TypedValue.COMPLEX_UNIT_SP, this)

private fun getTypedValue(unit: Int, value: Float): Float {
    return TypedValue.applyDimension(unit, value, Resources.getSystem().displayMetrics)
}
