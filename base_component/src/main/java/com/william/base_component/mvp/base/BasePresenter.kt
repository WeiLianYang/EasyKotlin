package com.william.base_component.mvp.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.orhanobut.logger.Logger
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.reflect.ParameterizedType

/**
 * @author William
 * @date 2020/5/2 11:28
 * Class Comment：BasePresenter
 */
open class BasePresenter<V : IBaseView, M : IBaseModel> : IBasePresenter {

    protected var view: V? = null

    protected var model: M? = null

    private var mCompositeDisposable: CompositeDisposable? = null

    override fun getBaseView(): IBaseView? = view

    override fun getBaseModel(): IBaseModel? = model

    @Suppress("UNCHECKED_CAST")
    override fun attachView(iView: IBaseView?) {
        this.view = iView as? V
        try {
            val params =
                (this.javaClass.genericSuperclass as ParameterizedType?)!!.actualTypeArguments
            model = (params[1] as Class<*>).newInstance() as M
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (view is LifecycleOwner) {
            val viewOwner = view as LifecycleOwner
            viewOwner.lifecycle.addObserver(this)
            Logger.d("BasePresenter addObserver")
            if (model is LifecycleObserver) {
                viewOwner.lifecycle.addObserver(model as LifecycleObserver)
                Logger.d("BaseModel addObserver")
            }
        }
    }

    override fun detachView() {
        model?.onDetach()
        view = null
        model = null
        Logger.d("BasePresenter detachView")
    }

    override fun addDisposable(disposable: Disposable?) {
        if (this.mCompositeDisposable == null) {
            this.mCompositeDisposable = CompositeDisposable()
        }
        disposable?.let {
            mCompositeDisposable?.add(it)
            Logger.d("BasePresenter addDisposable")
        }
    }

    override fun removeDisposable(disposable: Disposable?) {
        disposable?.let { this.mCompositeDisposable?.remove(it) }
    }

    override fun removeAllDisposable() {
        mCompositeDisposable?.clear()
        mCompositeDisposable = null
    }

    override fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
        Logger.d("BasePresenter解除OnLifecycleEvent监听$owner")
    }

}