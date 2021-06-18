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
import com.orhanobut.logger.Logger
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author William
 * @date 2020/5/19 10:46
 * Class Comment：BaseModel
 */
abstract class BaseModel : IBaseModel, LifecycleObserver {

    private var mCompositeDisposable: CompositeDisposable? = null

    override fun addDisposable(disposable: Disposable?) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        disposable?.let {
            mCompositeDisposable?.add(it)
            Logger.d("BaseModel addDisposable")
        }

    }

    override fun onDetach() {
        mCompositeDisposable?.clear()
        mCompositeDisposable = null
        Logger.d("BaseModel onDetach")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
        Logger.d("BaseModel解除OnLifecycleEvent监听$owner")
    }

}