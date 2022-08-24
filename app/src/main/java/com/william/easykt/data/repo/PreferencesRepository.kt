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

package com.william.easykt.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.william.base_component.extension.get
import com.william.base_component.extension.logD
import com.william.base_component.extension.logW
import com.william.base_component.extension.put
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine


/**
 * @author William
 * @date 2022/8/23 21:46
 * Class Comment：
 */
class PreferencesRepository(private val dataStore: DataStore<Preferences>) {

    private val intKey = intPreferencesKey("key_pref_int")

    private val longKey = longPreferencesKey("key_pref_long")

    private val floatKey = floatPreferencesKey("key_pref_float")

    private val doubleKey = doublePreferencesKey("key_pref_double")

    private val boolKey = booleanPreferencesKey("key_pref_bool")

    private val stringKey = stringPreferencesKey("key_pref_string")

    private val stringSetKey = stringSetPreferencesKey("key_pref_string_set")

    private val intFlow = dataStore.get(intKey, 0)

    private val longFlow = dataStore.get(longKey, 0L)

    private val floatFlow = dataStore.get(floatKey, .0f)

    private val doubleFlow = dataStore.get(doubleKey, .0)

    private val boolFlow = dataStore.get(boolKey, false)

    private val stringFlow = dataStore.get(stringKey, "")

    private val stringSetFlow = dataStore.get(stringSetKey, setOf())

    val combineFlow: Flow<String> = combine(
        intFlow, longFlow, floatFlow, doubleFlow,
        boolFlow, stringFlow, stringSetFlow
    ) { array ->
        val builder = StringBuilder()
        array.forEach { item ->
            when (item) {
                is Int -> builder.append("Int value: ")
                is Long -> builder.append("Long value: ")
                is Float -> builder.append("Float value: ")
                is Double -> builder.append("Double value: ")
                is Boolean -> builder.append("Boolean value: ")
                is String -> builder.append("String value: ")
                is Set<*> -> builder.append("Set<String> value: ")
            }
            builder.append(item).append("\n")
        }
        "builder: $builder".logD()
        return@combine builder.toString()
    }

    suspend fun savePreferencesData(value: Any?) {
        when (value) {
            is Int -> dataStore.put(intKey, value)
            is Long -> dataStore.put(longKey, value)
            is Float -> dataStore.put(floatKey, value)
            is Double -> dataStore.put(doubleKey, value)
            is Boolean -> dataStore.put(boolKey, value)
            is String -> dataStore.put(stringKey, value)
            is Set<*> -> {
                @Suppress("UNCHECKED_CAST")
                dataStore.put(stringSetKey, value as Set<String>)
            }
            else -> "can not save value".logW()
        }
    }
}