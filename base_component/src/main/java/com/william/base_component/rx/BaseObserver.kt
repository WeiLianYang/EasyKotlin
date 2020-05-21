package com.william.base_component.rx

import com.orhanobut.logger.Logger
import com.william.base_component.mvp.base.IBaseModel
import com.william.base_component.mvp.base.IBasePresenter
import com.william.base_component.mvp.base.IBaseView
import com.william.base_component.net.exception.ExceptionHandle
import com.william.base_component.net.exception.NetCode
import com.william.base_component.net.exception.ApiException
import com.william.base_component.net.response.BaseEntity
import io.reactivex.observers.DisposableObserver


/**
 * @author William
 * @date 2020/5/19 13:57
 * Class Comment：
 */
abstract class BaseObserver<T : BaseEntity?> constructor(
    private var basePresenter: IBasePresenter? = null,
    var showLoadingView: Boolean = true
) : DisposableObserver<T>() {

    private var baseView: IBaseView? = null
    private var baseModel: IBaseModel? = null

    init {
        baseView = basePresenter?.getBaseView()
        baseModel = basePresenter?.getBaseModel()
    }

    override fun onStart() {
        super.onStart()
        basePresenter?.addDisposable(this)
        baseModel?.addDisposable(this)
        if (showLoadingView) baseView?.startLoadingView()
    }

    override fun onComplete() {
        basePresenter?.removeDisposable(this)
    }

    override fun onNext(t: T) {
        baseView?.hideLoadingView()
        if (t == null) {
            return
        }
        when (t.errorCode) {
            NetCode.SUCCESS -> onSuccess(t)
            NetCode.TOKEN_EXPIRED -> baseView?.logBackIn()
            else -> baseView?.showToast(t.errorMsg)
        }
    }

    override fun onError(e: Throwable) {
        baseView?.hideLoadingView()
        val exception: ApiException? = ExceptionHandle.handleException(e)
        onFail(exception?.code, exception?.message)

        Logger.e("api error, code: ${exception?.code}, message: ${exception?.message}")
    }

    abstract fun onSuccess(entity: T?)

    abstract fun onFail(code: Int?, msg: String?)

}