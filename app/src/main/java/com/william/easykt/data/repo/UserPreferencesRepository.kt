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
import kotlin.random.Random


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

    suspend fun getUserPreferences(): UserPreferences = dataStore.data.first()

    suspend fun saveUserPreferencesData(preferences: UserPreferences) {
        dataStore.updateData { currentPref ->
            val innerMsg =
                UserPreferences.InnerMsg.newBuilder().setInnerKey1(Random.nextInt(100))
                    .setInnerKey2("222,${Random.nextInt(100)}").build()

            currentPref.toBuilder()
                .setVariableInt32(preferences.variableInt32)
                .setVariableInt64(preferences.variableInt64)
                .setVariableFloat(preferences.variableFloat)
                .setVariableDouble(preferences.variableDouble)
                .setVariableBool(preferences.variableBool)
                .setVariableString(preferences.variableString)
                .setSeason(preferences.season)
//                .putVarMap("key1", 1)
//                .putVarMap("key2", 2)
                .putAllVarMap(
                    mapOf(
                        Pair("key1", Random.nextInt(100)),
                        Pair("key2", Random.nextInt(100))
                    )
                )
                .setInnerMsg(innerMsg)
                .build()
        }
    }

    suspend fun saveUserPreferencesDataKt(preferences: UserPreferences) {
        dataStore.updateData { currentPref ->
            val inner_msg = UserPreferences.InnerMsg.newBuilder().apply {
                innerKey1 = Random.nextInt(100)
                innerKey2 = "222,${Random.nextInt(100)}"
            }.build()

            currentPref.toBuilder().apply {
                variableInt32 = preferences.variableInt32
                variableInt64 = preferences.variableInt64
                variableFloat = preferences.variableFloat
                variableDouble = preferences.variableDouble
                variableBool = preferences.variableBool
                variableString = preferences.variableString
                season = preferences.season
                putAllVarMap(
                    mapOf(
                        Pair("key1", Random.nextInt(100)),
                        Pair("key2", Random.nextInt(100))
                    )
                )
                innerMsg = inner_msg
            }.build()
        }
    }

}