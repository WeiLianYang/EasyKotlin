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

import android.util.Log
import com.william.base_component.BuildConfig

const val TAG = "EasyKotlin"

var enableLog = BuildConfig.DEBUG

private enum class LEVEL {
    V, D, I, W, E
}

fun String.logV(tag: String = TAG) =
    log(LEVEL.V, tag, this)

fun String.logD(tag: String = TAG) =
    log(LEVEL.D, tag, this)

fun String.logI(tag: String = TAG) =
    log(LEVEL.I, tag, this)

fun String.logW(tag: String = TAG) =
    log(LEVEL.W, tag, this)

fun String.logE(tag: String = TAG) =
    log(LEVEL.E, tag, this)

private fun log(level: LEVEL, tag: String, msg: String) {
    if (!enableLog) return
    val message = getMessage(msg)
    when (level) {
        LEVEL.V -> Log.v(tag, message)
        LEVEL.D -> Log.d(tag, message)
        LEVEL.I -> Log.i(tag, message)
        LEVEL.W -> Log.w(tag, message)
        LEVEL.E -> Log.e(tag, message)
    }
}

private fun getMessage(msg: String): String {
    val name = getMethodName()
    return if (name == null) msg else "$name -> $msg"
}

private fun getMethodName(): String? {
    val stackTraceArray = Thread.currentThread().stackTrace ?: return null
    for (stackTrace in stackTraceArray) {
        if (stackTrace.isNativeMethod) {
            continue
        }
        if (stackTrace.className == Thread::class.java.name || stackTrace.className.contains("LogExt")) {
            continue
        }
        return "[${Thread.currentThread().name}(${Thread.currentThread().id}): ${stackTrace.fileName}:${stackTrace.lineNumber}]"
    }
    return null
}
