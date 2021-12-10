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

package com.william.easykt.test

import androidx.lifecycle.*
import com.william.base_component.extension.launchFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

/**
 *  author : WilliamYang
 *  date : 2021/8/17 14:22
 *  description :
 */
class TestViewModel(private val repository: TestRepository) : ViewModel() {

    val testData = MutableLiveData<String>()

    fun requestData() {
        launchFlow(
            { repository.getData() },
            { testData.value = it.toString() }
        )
    }

    companion object {

        fun provideFactory(repository: TestRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(TestViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return TestViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}