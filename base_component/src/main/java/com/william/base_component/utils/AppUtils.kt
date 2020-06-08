package com.william.base_component.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.william.base_component.BaseApp

/**
 * @author William
 * @date 2020/4/22 20:20
 * Class Comment：
 */

/**
 * 获取手机系统SDK版本
 *
 * @return 如API 17 则返回 17
 */
val sdkVersion: Int
    get() = android.os.Build.VERSION.SDK_INT

/**
 * 得到软件版本号
 *
 * @return 当前版本Code
 */
fun getAppVersionCode(): Int {
    var verCode = -1
    try {
        val packageName = BaseApp.instance.packageName
        verCode = BaseApp.instance.packageManager
            .getPackageInfo(packageName, 0).versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return verCode
}

/**
 * 得到软件显示版本信息
 *
 * @return 当前版本信息
 */
fun getAppVersionName(): String {
    var verName = ""
    try {
        val packageName = BaseApp.instance.packageName
        verName = BaseApp.instance.packageManager.getPackageInfo(packageName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return verName
}

/**
 * 打开某个activity，针对于不需要参数跳转的可以用这个
 * @param context 上下文
 * @param clazz 目标activity的class对象
 */
fun <T> openActivity(context: Context, clazz: Class<T>) {
    val intent = Intent(context, clazz)
    context.startActivity(intent)
}
