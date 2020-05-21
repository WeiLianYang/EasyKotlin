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