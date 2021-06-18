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

package com.william.base_component.rx

import com.william.base_component.rx.scheduler.*

fun <T> ioToMain(): IoMainScheduler<T> {
    return IoMainScheduler()
}

fun <T> ioToIo(): IoIoScheduler<T> {
    return IoIoScheduler()
}

fun <T> computationToMain(): ComputationMainScheduler<T> {
    return ComputationMainScheduler()
}

fun <T> singleToMain(): SingleMainScheduler<T> {
    return SingleMainScheduler()
}

fun <T> trampolineToMain(): TrampolineMainScheduler<T> {
    return TrampolineMainScheduler()
}

fun <T> newThreadToMain(): NewThreadMainScheduler<T> {
    return NewThreadMainScheduler()
}