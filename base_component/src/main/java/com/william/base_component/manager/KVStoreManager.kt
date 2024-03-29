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

package com.william.base_component.manager

import android.content.Context
import android.os.Parcelable
import com.tencent.mmkv.MMKV
import com.william.base_component.extension.logD
import java.util.*

/**
 * author : WilliamYang
 * date : 2021/5/14 10:01
 * description : MMKV DataStore
 */
object KVStoreManager {

    /**
     * 区别业务存储，设置多进程访问
     */
    val mmkv: MMKV? by lazy {
        MMKV.mmkvWithID("easyKt", MMKV.MULTI_PROCESS_MODE)
    }

    fun init(context: Context) {
        val rootDir: String = MMKV.initialize(context.applicationContext)
        rootDir.logD()
    }

    fun put(key: String, value: Any?) {
        when (value) {
            is String -> mmkv?.encode(key, value)
            is Float -> mmkv?.encode(key, value)
            is Boolean -> mmkv?.encode(key, value)
            is Int -> mmkv?.encode(key, value)
            is Long -> mmkv?.encode(key, value)
            is Double -> mmkv?.encode(key, value)
            is ByteArray -> mmkv?.encode(key, value)
            is Nothing -> return
        }
    }

    fun <T : Parcelable> put(key: String, t: T?) {
        if (t == null) {
            return
        }
        mmkv?.encode(key, t)
    }

    fun put(key: String, sets: Set<String>?) {
        if (sets == null) {
            return
        }
        mmkv?.encode(key, sets)
    }

    fun getInt(key: String): Int? {
        return mmkv?.decodeInt(key, 0)
    }

    fun getDouble(key: String): Double? {
        return mmkv?.decodeDouble(key, 0.00)
    }

    fun getLong(key: String): Long? {
        return mmkv?.decodeLong(key, 0L)
    }

    fun getBoolean(key: String): Boolean? {
        return mmkv?.decodeBool(key, false)
    }

    fun getFloat(key: String): Float? {
        return mmkv?.decodeFloat(key, 0F)
    }

    fun getByteArray(key: String): ByteArray? {
        return mmkv?.decodeBytes(key)
    }

    fun getString(key: String): String? {
        return mmkv?.decodeString(key, "")
    }

    fun <T : Parcelable> getParcelable(key: String, tClass: Class<T>): T? {
        return mmkv?.decodeParcelable(key, tClass)
    }

    fun getStringSet(key: String): Set<String>? {
        return mmkv?.decodeStringSet(key, Collections.emptySet())
    }

    fun containsKey(key: String): Boolean {
        return mmkv?.containsKey(key) ?: false
    }

    fun removeKey(key: String) {
        mmkv?.removeValueForKey(key)
    }

    fun clearAll() {
        mmkv?.clearAll()
    }
}