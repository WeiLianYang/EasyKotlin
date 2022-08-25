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
import com.william.base_component.extension.logE
import com.william.easykt.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.io.IOException


/**
 * author：William
 * date：2022/8/25 22:17
 * description：
 */
class UserPreferencesRepository(private val dataStore: DataStore<UserPreferences>) {

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { e ->
            if (e is IOException) {
                "Error reading preferences. $e".logE()
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw e
            }
        }

    suspend fun getPreferencesData() = dataStore.data.first()

    suspend fun saveUserPreferencesData(value: Any?) {
        dataStore.updateData { currentPref ->
            when (value) {
                is Int -> currentPref.toBuilder().setVariableInt32(value).build()
                is Float -> currentPref.toBuilder().setVariableFloat(value).build()
                is Double -> currentPref.toBuilder().setVariableDouble(value).build()
                is Boolean -> currentPref.toBuilder().setVariableBool(value).build()
                is String -> currentPref.toBuilder().setVariableString(value).build()
                is UserPreferences.Season -> currentPref.toBuilder().setSeason(value).build()
                else -> currentPref.toBuilder().build()
            }
        }
    }

}