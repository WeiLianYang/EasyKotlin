package com.william.base_component.mvp.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.reflect.ParameterizedType

/**
 * @author William
 * @date 2020/5/2 11:28
 * Class Comment：
 */
open class BasePresenter<V : IBaseView?, M : IBaseModel> : IBasePresenter {

    protected var view: V? = null

    protected var model: M? = null

    private var disposable: CompositeDisposable? = null

    @Suppress("UNCHECKED_CAST")
    override fun attachView(view: Any) {
        this.view = view as V
        try {
            val params =
                (this.javaClass.genericSuperclass as ParameterizedType?)!!.actualTypeArguments
            model = (params[1] as Class<*>).newInstance() as M
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun detachView() {
        view = null
        model = null
    }

    override fun addDisposable(disposable: Disposable?) {
        if (this.disposable == null) {
            this.disposable = CompositeDisposable()
        }
        this.disposable!!.add(disposable!!)
    }

    override fun removeDisposable(disposable: Disposable?) {
        this.disposable?.remove(disposable!!)
    }

    override fun removeAllDisposable() {
        disposable?.clear()
    }

}