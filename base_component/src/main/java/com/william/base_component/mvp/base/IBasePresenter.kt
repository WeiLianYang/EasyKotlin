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

package com.william.base_component.mvp.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.Disposable

/**
 * Author：William Time：2018/8/13
 * Class Comment：IBasePresenter
 */
interface IBasePresenter : LifecycleObserver {

    fun getBaseView(): IBaseView?

    fun getBaseModel(): IBaseModel?

    fun attachView(iView: IBaseView?)

    fun detachView()

    fun addDisposable(disposable: Disposable?)

    fun removeDisposable(disposable: Disposable?)

    fun removeAllDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner)

}