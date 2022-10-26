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

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Outline
import android.net.Uri
import android.os.Build
import android.os.Process
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.View
import android.view.ViewOutlineProvider
import com.william.base_component.BaseApp
import com.william.base_component.extension.dp
import com.william.base_component.extension.logD
import com.william.base_component.extension.logE
import java.util.*

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
 * 打开某个activity，传值，也可不传值
 * @param context 上下文
 * @param block 传参表达式
 * @param T 目标activity
 */
inline fun <reified T : Activity> openActivity(
    context: Context,
    noinline block: (Intent.() -> Unit) = {}
) {
    val intent = Intent(context, T::class.java)
    intent.block()
    context.startActivity(intent)
}

/**
 * 设置视图裁剪的圆角半径
 * @param radius
 */
fun setClipViewCornerRadius(view: View?, radius: Int) {
    if (radius <= 0) {
        return
    }
    view?.apply {
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(v: View, outline: Outline) {
                outline.setRoundRect(0, 0, v.width, v.height, radius.dp.toFloat());
            }
        }
        clipToOutline = true
    }

}


/**
 * 打开浏览器
 */
fun openBrowser(
    context: Context,
    url: String?,
    packageName: String? = null,
    className: String? = null
) {
    if (url.isNullOrEmpty()) {
        return
    }
    val uri = Uri.parse(url)
    runCatching {
        if (!packageName.isNullOrEmpty() && !className.isNullOrEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setClassName(packageName, className)
            context.startActivity(intent)
        }
    }.onSuccess {
        "open browser success".logD()
    }.onFailure {
        "open browser failed : ${it.message}".logE()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }
}

fun openBrowser(context: Context, url: String?) {
    if (url.isNullOrEmpty()) {
        return
    }
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    runCatching {
        val resolveList = context.packageManager.queryIntentActivities(intent, 0)
        "queryIntent resolveList size: ${resolveList.size}".logD()
        val hasAvailableBrowser = resolveList.isNotEmpty()
        if (hasAvailableBrowser) {
            intent.apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
        hasAvailableBrowser
    }.onSuccess {
        "open browser result : $it".logD()
    }.onFailure {
        "open browser failed : ${it.message}".logE()
    }
}

fun Uri?.isNotEmpty(): Boolean {
    return this != null && this.toString().isNotEmpty()
}

/**
 * 获取国家地区代码
 * @param context 上下文
 * @return 返回此语言环境的国家/地区代码，该代码应该是空字符串、大写的 ISO 3166 2 位字母代码或 UN M.49 3 位数字代码。
 */
fun getCountryCode(context: Context): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales.get(0).country
    } else {
        context.resources.configuration.locale.country
    }
}

/**
 * 判断国家是否是国内用户
 */
fun isCN(context: Context): Boolean {
    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    var countryIso = tm.simCountryIso
    var isCN = false
    if (!TextUtils.isEmpty(countryIso)) {
        countryIso = countryIso.uppercase(Locale.US)
        if (countryIso.contains("CN")) {
            isCN = true
        }
    }
    return isCN
}

/**
 * 检索可以为给定意图执行的所有 activity
 */
fun queryIntent(context: Context, intent: Intent): Boolean {
    val list: List<ResolveInfo>
    context.packageManager.apply {

        list = queryIntentActivities(intent, PackageManager.MATCH_ALL)

        // 检索可以匹配给定意图的所有服务
        queryIntentServices(intent, PackageManager.MATCH_ALL)

        // 检索可以处理给定意图广播的所有接收器
        queryBroadcastReceivers(intent, PackageManager.MATCH_ALL)

        // 查询内容提供者
        queryContentProviders("", Process.myUid(), PackageManager.MATCH_ALL)
    }
    list.forEach {
        "queryIntent, item: ${it.activityInfo}".logD()
    }
    return list.isNotEmpty()
}

/**
 * 获取当前用户安装的所有软件包的列表。
 */
fun getInstalledPackages(context: Context): Boolean {
    val list: List<PackageInfo>
    context.packageManager.apply {
        list = getInstalledPackages(PackageManager.GET_META_DATA)

        // 获取已安装的应用程序
        getInstalledApplications(PackageManager.GET_META_DATA)
    }
    list.forEach {
        "getInstalledPackages, item: ${it.applicationInfo.className}".logD()
    }
    return list.isNotEmpty()
}

/**
 * 检索有关系统上安装的应用程序包的整体信息
 */
fun getPackageInfo(context: Context, packageName: String): Boolean {
    var packageInfo: PackageInfo? = null
    runCatching {
        packageInfo =
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        "getPackageInfo: ${packageInfo?.applicationInfo?.className}".logD()
    }.onFailure {
        "getPackageInfo failed, $it".logE()
    }
    return packageInfo != null
}

fun isLargerThanVersion(version: Int): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
}

fun isSmallerThanVersion(version: Int): Boolean {
    return Build.VERSION.SDK_INT < version
}
