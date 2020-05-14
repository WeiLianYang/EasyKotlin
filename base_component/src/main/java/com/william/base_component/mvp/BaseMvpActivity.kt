package com.william.base_component.mvp

import android.os.Bundle
import com.william.base_component.activity.BaseActivity
import com.william.base_component.mvp.base.BasePresenter
import com.william.base_component.mvp.base.IBaseModel
import com.william.base_component.mvp.base.IBaseView
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author William
 * @date 2020/5/2 11:32
 * Class Comment：
 */
abstract class BaseMvpActivity<P : BasePresenter<out IBaseView, out IBaseModel>> : BaseActivity() {

    var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initPresenter()
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        mPresenter?.detachView()
        mPresenter?.removeAllDisposable()
        super.onDestroy()
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

}