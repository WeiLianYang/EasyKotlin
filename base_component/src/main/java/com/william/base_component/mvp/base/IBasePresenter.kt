package com.william.base_component.mvp.base

import io.reactivex.disposables.Disposable

/**
 * Author：William Time：2018/8/13
 * Class Comment：
 */
interface IBasePresenter {

    fun attachView(view: Any)

    fun detachView()

    fun addDisposable(disposable: Disposable?)

    fun removeDisposable(disposable: Disposable?)

    fun removeAllDisposable()
}