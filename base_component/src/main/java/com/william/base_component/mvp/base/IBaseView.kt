package com.william.base_component.mvp.base

import android.app.Activity


/**
 * @author William
 * @date 2020/5/2 10:18
 * Class Comment：
 */
interface IBaseView {

    fun getCurrentActivity(): Activity

    fun showToast(msg: String)

    fun startLoading(isCanNotCancel: Boolean = false)

    fun cancelLoading()

    fun onFailed(action: Int, errorCode: Int, message: String)

}