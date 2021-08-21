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

package com.william.base_component.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.william.base_component.utils.logD
import com.william.base_component.utils.logE
import com.william.base_component.utils.logI
import com.william.base_component.utils.logW
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 *  author : WilliamYang
 *  date : 2021/8/17 13:47
 *  description :
 */

fun <T> ViewModel.launchFlow(
    block: suspend () -> T,
    success: (T) -> Unit = {},
    error: (Throwable) -> Unit = {}
) {
    viewModelScope.launch {
        flow {
            "flow emit value".logD()
            emit(block())
        }.flowOn(Dispatchers.Default)
            .onCompletion { cause -> "flow completed with $cause".logW() } // 2 观察上下游异常
            .catch { ex ->
                // 3 需要放在 onCompletion 之后，否则发生异常后 onCompletion 不执行
                "flow caught $ex".logE()
                error(ex)
            }.collect { value ->  // 1
                "flow collect value".logI()
                success(value)
            }
    }
}
