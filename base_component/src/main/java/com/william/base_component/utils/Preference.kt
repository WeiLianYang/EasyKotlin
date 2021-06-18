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

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.william.base_component.BaseApp
import java.io.*
import kotlin.reflect.KProperty

/**
 * @author William
 * @date 2020/4/22 20:18
 * Class Comment：委托属性 + SharedPreference
 */
class Preference<T>(val name: String, private val default: T) {

    companion object {

        private val file_name = getAppVersionName()

        private val prefs: SharedPreferences by lazy {
            BaseApp.instance.getSharedPreferences(file_name, Context.MODE_PRIVATE)
        }

        /**
         * 删除全部数据
         */
        fun clearPreference() {
            prefs.edit().clear().apply()
        }

        /**
         * 根据key删除存储数据
         */
        fun clearPreference(key: String) {
            prefs.edit().remove(key).apply()
        }
    }


    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getSharedPreferences(name, default)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putSharedPreferences(name, value)
    }

    @SuppressLint("CommitPrefEdits")
    private fun putSharedPreferences(name: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> putString(name, serialize(value))
        }.apply()
    }

    @Suppress("UNCHECKED_CAST")
    private fun getSharedPreferences(name: String, default: T): T = with(prefs) {
        val res: Any? = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> {
                val str = getString(name, serialize(default))
                str?.let {
                    deSerialization<String>(it)
                }
            }
        }
        return res as T
    }


    /**
     * 序列化对象
     */
    @Throws(IOException::class)
    private fun <Any> serialize(obj: Any?): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(obj)
        var serStr = byteArrayOutputStream.toString("ISO-8859-1")
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8")
        objectOutputStream.close()
        byteArrayOutputStream.close()
        return serStr
    }

    /**
     * 反序列化对象
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IOException::class, ClassNotFoundException::class)
    private fun <Any> deSerialization(str: String): Any? {
        val redStr = java.net.URLDecoder.decode(str, "UTF-8")
        val byteArrayInputStream = ByteArrayInputStream(
            redStr.toByteArray(charset("ISO-8859-1"))
        )
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as? Any
        objectInputStream.close()
        byteArrayInputStream.close()
        return obj
    }


    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return boolean
     */
    fun contains(key: String): Boolean {
        return prefs.contains(key)
    }

    /**
     * 返回所有的键值对
     */
    fun getAll(): Map<String, *> {
        return prefs.all
    }
}
