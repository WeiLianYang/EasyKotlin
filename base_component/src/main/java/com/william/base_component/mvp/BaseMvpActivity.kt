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
 * Class Comment：BaseMvpActivity
 */
abstract class BaseMvpActivity<P : BasePresenter<out IBaseView, out IBaseModel>> : BaseActivity() {

    lateinit var mPresenter: P

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
        mPresenter.attachView(this)
    }

    override fun onDestroy() {
        mPresenter.run {
            detachView()
            removeAllDisposable()
        }
        super.onDestroy()
    }

}