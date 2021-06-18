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

package com.william.base_component.rx

import com.orhanobut.logger.Logger
import com.william.base_component.alias.FailCallback
import com.william.base_component.alias.SuccessCallback
import com.william.base_component.mvp.base.IBaseModel
import com.william.base_component.mvp.base.IBasePresenter
import com.william.base_component.mvp.base.IBaseView
import com.william.base_component.net.data.BaseResponse
import com.william.base_component.net.exception.ApiException
import com.william.base_component.net.exception.SUCCESS
import com.william.base_component.net.exception.TOKEN_EXPIRED
import com.william.base_component.net.exception.handleException
import io.reactivex.observers.DisposableObserver


/**
 * @author William
 * @date 2020/5/19 13:57
 * Class Comment：
 */
class BaseObserver<T> constructor(
    private var basePresenter: IBasePresenter? = null,
    private var showLoading: Boolean = true,
    private var onSuccess: SuccessCallback<T>? = null,
    private var onFail: FailCallback? = null
) : DisposableObserver<BaseResponse<T>>() {

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
        if (showLoading) baseView?.showLoadingView()
    }

    override fun onComplete() {
        basePresenter?.removeDisposable(this)
    }

    override fun onNext(response: BaseResponse<T>) {
        baseView?.hideLoadingView()

        when (response.errorCode) {
            SUCCESS -> onSuccess?.invoke(response)
            TOKEN_EXPIRED -> baseView?.logBackIn()
            else -> {
                baseView?.showToast(response.errorMsg)
                onFail?.invoke(response.errorCode, response.errorMsg)
            }
        }
    }

    override fun onError(e: Throwable) {
        baseView?.hideLoadingView()
        val exception: ApiException? = handleException(e)
        exception?.let {
            onFail?.invoke(it.code, it.message)
            Logger.e("api error, code: ${it.code}, message: ${it.message}")
        }
    }

}