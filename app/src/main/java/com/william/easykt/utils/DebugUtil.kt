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

package com.william.easykt.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Debug
import android.os.Process
import com.william.easykt.BuildConfig
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.*
import kotlin.system.exitProcess

/**
 * @author William
 * @date 2022/4/12 14:35
 * Class Comment：
 */

/** 判断当前应用是否是debug状态 */
fun isDebuggable(context: Context): Boolean {
    return 0 != context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
}

/** 检测是否在非Debug编译模式下，进行了调试操作，以防动态调试 */
fun checkTraced(context: Context) {
    //非Debug 编译，反调试检测
    if (!BuildConfig.DEBUG) {
        if (isDebuggable(context)) {
            exitProcess(0)
        }
        val thread = Thread({
            while (true) {
                try {
                    // 每隔300ms检测一次
                    Thread.sleep(300)
                    // 判断是否有调试器连接，是就退出
                    if (Debug.isDebuggerConnected()) {
                        exitProcess(0)
                    }

                    // 判断是否被其他进程跟踪，是就退出
                    if (isUnderTraced()) {
                        exitProcess(0)
                    }
                } catch (e: InterruptedException) {
                    //
                }
            }
        }, "SafeGuardThread")

        thread.start()
    }

    if (isUnderTraced()) {
        exitProcess(0)
    }
}

/**
 * 当我们使用Ptrace方式跟踪一个进程时，目标进程会记录自己被谁跟踪，可以查看/proc/pid/status看到这个信息,而没有被调试的时候TracerPid为0
 */
fun isUnderTraced(): Boolean {
    val processStatusFilePath = String.format(Locale.US, "/proc/%d/status", Process.myPid())
    val procInfoFile = File(processStatusFilePath)
    try {
        val b = BufferedReader(FileReader(procInfoFile))
        var readLine: String?
        while (b.readLine().also { readLine = it } != null) {
            if (readLine?.contains("TracerPid") == true) {
                val arrays = readLine!!.split(":").toTypedArray()
                if (arrays.size == 2) {
                    val tracerPid = arrays[1].trim { it <= ' ' }.toInt()
                    if (tracerPid != 0) {
                        return true
                    }
                }
            }
        }
        b.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}