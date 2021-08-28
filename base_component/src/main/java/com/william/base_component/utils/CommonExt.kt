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

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.william.base_component.BaseApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 *  author : WilliamYang
 *  date : 2021/8/12 17:57
 *  description :
 */

val Context.dataStore by preferencesDataStore("easyKt_prefs")

suspend fun <T> DataStore<Preferences>.put(key: Preferences.Key<T>, value: T) {
    edit { preferences ->
        preferences[key] = value
    }
}

fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>, defaultVal: T): Flow<T> {
    return data.catch { exception ->
        // dataStore.data throws an IOException when an error is encountered when reading data
        if (exception is IOException) {
            "Error reading preferences, ${exception.message}".logE()
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[key] ?: defaultVal
    }
}

fun String?.getNoNull(default: String = ""): String {
    return this ?: default
}

fun Int.toText(): String = BaseApp.instance.getString(this)

fun Int.toPx(): Int = BaseApp.instance.resources.getDimensionPixelOffset(this)
