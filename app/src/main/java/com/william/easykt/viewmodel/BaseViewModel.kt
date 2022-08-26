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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.full.primaryConstructor


/**
 * @author William
 * @date 2022/8/23 22:05
 * Class Comment：
 */
open class BaseViewModel : ViewModel()

/**
 * 创建 ViewModelProvider.Factory
 */
inline fun <reified VM : BaseViewModel> provideFactory(vararg repository: Any): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val clazz = VM::class
            if (modelClass.isAssignableFrom(clazz.java)) {
                @Suppress("UNCHECKED_CAST")
                return clazz.primaryConstructor?.call(*repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
