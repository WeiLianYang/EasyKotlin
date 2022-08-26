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

package com.william.easykt.viewmodel

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.william.easykt.UserPreferences
import com.william.easykt.data.repo.PreferencesRepository
import com.william.easykt.data.repo.UserPreferencesRepository
import kotlinx.coroutines.launch


/**
 * author：William
 * date：2022/8/23 21:36
 * description：data store view model
 */
class DataStoreViewModel(
    private val repository: PreferencesRepository,
    private val userRepo: UserPreferencesRepository
) : BaseViewModel() {

    val preferencesData = repository.combineFlow.asLiveData()

    val userPreferencesData = userRepo.userPreferencesFlow.asLiveData()

    /** 保存 Preferences 数据 */
    fun savePreferencesData(value: Any?) {
        viewModelScope.launch {
            repository.savePreferencesData(value)
        }
    }

    /** 保存 Proto 数据 */
    fun saveUserPreferencesData(preferences: UserPreferences) {
        viewModelScope.launch {
            userRepo.saveUserPreferencesData(preferences)
        }
    }

}