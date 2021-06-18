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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.william.base_component.fragment.BaseFragment
import com.william.base_component.mvp.base.BasePresenter
import com.william.base_component.mvp.base.IBaseModel
import com.william.base_component.mvp.base.IBaseView
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


/**
 * @author William
 * @date 2020/4/22 19:05
 * Class Comment：BaseMvpFragment
 */
abstract class BaseMvpFragment<P : BasePresenter<out IBaseView, out IBaseModel>> : BaseFragment() {

    lateinit var mPresenter: P

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initPresenter()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun initPresenter() {
        // TODO: William 2020/5/22 19:10 后续可以试下用属性委托的方式丢出去，和BaseMvpActivity共用
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