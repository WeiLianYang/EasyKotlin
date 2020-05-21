package com.william.base_component.mvp.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.Disposable


/**
 * @author William
 * @date 2020/5/2 10:29
 * Class Comment：IBaseModel
 */
interface IBaseModel : LifecycleObserver {

    fun addDisposable(disposable: Disposable?)

    fun onDetach()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner)
}