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

package com.william.base_component.rx.scheduler

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * @author William
 * @date 2020/5/19 13:54
 * Class Comment：
 */

class ComputationMainScheduler<T> :
    BaseScheduler<T>(Schedulers.computation(), AndroidSchedulers.mainThread())

class SingleMainScheduler<T> : BaseScheduler<T>(Schedulers.single(), AndroidSchedulers.mainThread())

class TrampolineMainScheduler<T> :
    BaseScheduler<T>(Schedulers.trampoline(), AndroidSchedulers.mainThread())

class NewThreadMainScheduler<T> :
    BaseScheduler<T>(Schedulers.newThread(), AndroidSchedulers.mainThread())

class IoMainScheduler<T> : BaseScheduler<T>(Schedulers.io(), AndroidSchedulers.mainThread())

class IoIoScheduler<T> : BaseScheduler<T>(Schedulers.io(), Schedulers.io())
