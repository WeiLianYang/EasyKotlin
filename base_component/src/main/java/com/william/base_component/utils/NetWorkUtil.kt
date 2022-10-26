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

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.*
import android.os.Build
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import com.william.base_component.extension.logD
import com.william.base_component.extension.logE
import com.william.base_component.extension.logV
import com.william.base_component.extension.logW
import java.io.IOException
import java.net.HttpURLConnection
import java.net.NetworkInterface
import java.net.SocketException
import java.net.URL


private var NET_AVAILABLE = 1
private var NET_TIMEOUT = 2
private var NET_NOT_PREPARE = 3
private var NET_ERROR = 4
private const val TIMEOUT = 3000

/**
 * check NetworkAvailable
 *
 * @param context
 * @return
 */
fun isNetworkAvailable(context: Context): Boolean {
    val manager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info = manager.activeNetworkInfo
    return !(null == info || !info.isAvailable)
}

/**
 * check NetworkConnected
 *
 * @param context
 * @return
 */
fun isNetworkConnected(context: Context): Boolean {
    val manager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info = manager.activeNetworkInfo
    return !(null == info || !info.isConnected)
}

/**
 * 得到ip地址
 *
 * @return
 */
fun getLocalIpAddress(): String {
    var ret = ""
    try {
        val en = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val enumIpAddress = en.nextElement().inetAddresses
            while (enumIpAddress.hasMoreElements()) {
                val netAddress = enumIpAddress.nextElement()
                if (!netAddress.isLoopbackAddress) {
                    ret = netAddress.hostAddress.toString()
                }
            }
        }
    } catch (ex: SocketException) {
        ex.printStackTrace()
    }

    return ret
}


/**
 * ping "http://www.baidu.com"
 *
 * @return
 */
private fun pingNetWork(): Boolean {
    var result = false
    var httpUrl: HttpURLConnection? = null
    try {
        httpUrl = URL("http://www.baidu.com")
            .openConnection() as HttpURLConnection
        httpUrl.connectTimeout = TIMEOUT
        httpUrl.connect()
        result = true
    } catch (e: IOException) {
    } finally {
        httpUrl?.disconnect()
    }
    return result
}

/**
 * isWifi
 *
 * @param context
 * @return boolean
 */
fun isWifiNet(context: Context): Boolean {
    val connectivityManager = context
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetInfo = connectivityManager.activeNetworkInfo
    return activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_WIFI
}

/**
 * is wifi on
 */
fun isWifiEnabled(context: Context): Boolean {
    val mgrConn: ConnectivityManager? = context
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val mgrTel = context
        .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    return if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        false
    } else mgrConn?.activeNetworkInfo != null && mgrConn.activeNetworkInfo?.state == NetworkInfo.State.CONNECTED || mgrTel
        .networkType == TelephonyManager.NETWORK_TYPE_UMTS
}

/**
 * 判断MOBILE网络是否可用
 */
fun isMobileNetAvailable(context: Context?): Boolean {
    if (context != null) {
        //获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
        val manager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //获取NetworkInfo对象
        val networkInfo = manager.activeNetworkInfo
        //判断NetworkInfo对象是否为空 并且类型是否为MOBILE
        if (null != networkInfo && networkInfo.type == ConnectivityManager.TYPE_MOBILE)
            return networkInfo.isAvailable
    }
    return false
}

fun monitorNetworkChange(context: Context) {
    val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    "manager: ${manager != null}".logV()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        manager?.registerDefaultNetworkCallback(CustomNetworkCallback())
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        manager?.registerNetworkCallback(
            NetworkRequest.Builder().build(), CustomNetworkCallback()
        )
    } else {
        // 注册广播
    }
}

class CustomNetworkCallback : ConnectivityManager.NetworkCallback() {
    override fun onLosing(network: Network, maxMsToLive: Int) {
        "onLosing, network: $network, maxMsToLive: $maxMsToLive".logW()
    }

    override fun onUnavailable() {
        "onUnavailable".logE()
    }

    override fun onLost(network: Network) {
        //
        "onLost, maxMsToLive: $network".logE()
    }

    override fun onCapabilitiesChanged(
        network: Network, capabilities: NetworkCapabilities
    ) {
        //
        "onCapabilitiesChanged, network: $network, networkCapabilities: $capabilities".logD()
    }

    override fun onLinkPropertiesChanged(network: Network, properties: LinkProperties) {
        //
        "onLinkPropertiesChanged, network: $network, properties: $properties".logD()
    }

    override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
        //
        "onBlockedStatusChanged, network: $network, blocked: $blocked".logD()
    }
}