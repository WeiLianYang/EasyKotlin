package com.william.base_component.mvp

import android.os.Bundle
import android.widget.Toast
import com.william.base_component.activity.BaseActivity
import com.william.base_component.fragment.LoadingDialog
import com.william.base_component.mvp.base.BasePresenter
import com.william.base_component.mvp.base.IBaseModel
import com.william.base_component.mvp.base.IBaseView
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author William
 * @date 2020/5/2 11:32
 * Class Comment：BaseMvpActivity
 */
abstract class BaseMvpActivity<P : BasePresenter<out IBaseView, out IBaseModel>> : BaseActivity(),
    IBaseView {

    var mPresenter: P? = null
    private var mLoadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initPresenter()
        super.onCreate(savedInstanceState)
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun initPresenter() {
        try {
            val type: Type? = this::class.java.genericSuperclass
            if (type is ParameterizedType) {
                mPresenter = (type.actualTypeArguments[0] as Class<*>).newInstance() as P
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mPresenter?.attachView(this)
    }

    override fun getCurrentActivity() = mActivity

    override fun logBackIn() {
        // 重新打开登录界面
    }

    override fun showToast(msg: String?) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show()
    }

    override fun startLoadingView(isCanNotCancel: Boolean) {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog()
        } else {
            mLoadingDialog?.dismissAllowingStateLoss()
        }
        mLoadingDialog?.canNotCancel = isCanNotCancel
        if (mLoadingDialog?.isAdded!!) {
            mLoadingDialog?.show(
                supportFragmentManager,
                LoadingDialog::class.java.simpleName
            )
        }
    }

    override fun hideLoadingView() {
        mLoadingDialog?.dismissAllowingStateLoss()
    }

    override fun onFailed(action: Int, errorCode: Int, message: String) {
    }

    override fun onDestroy() {
        hideLoadingView()
        mPresenter?.detachView()
        mPresenter?.removeAllDisposable()
        super.onDestroy()
    }

}