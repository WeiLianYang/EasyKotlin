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