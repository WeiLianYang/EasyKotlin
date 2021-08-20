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

package com.william.base_component.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.jeremyliao.liveeventbus.LiveEventBus
import com.william.base_component.databinding.BaseFragmentBinding
import com.william.base_component.extension.toast
import com.william.base_component.mvp.base.IBaseView


/**
 * @author William
 * @date 2020/5/21 19:46
 * Class Comment：Fragment base class
 */
abstract class BaseFragment : Fragment(), IBaseView {

    protected var mActivity: FragmentActivity? = null
    protected var mFragment: Fragment? = null

    protected lateinit var mContext: Context
    protected lateinit var baseBinding: BaseFragmentBinding
    protected abstract val viewBinding: ViewBinding

    private var hasLoadData = false
    private var mLoadingDialog: LoadingDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity
        mFragment = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        baseBinding = BaseFragmentBinding.inflate(layoutInflater)
        val rootView = baseBinding.root
        initLayout()
        initView(rootView)
        initAction()
        initEvent()

        return rootView
    }

    override fun onResume() {
        super.onResume()
        lazyLoad()
    }

    private fun lazyLoad() {
        if (!hasLoadData) {
            hasLoadData = true
            sendRequest()
        }
    }

    private fun initLayout() {
        if (getLayoutId() != 0) {
            val params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            viewBinding.root.parent?.let {
                // 因为mViewBinding只被初始化一次，下次访问时还是上次的值，所以需要将它的父View和其解除关系，避免被重新addView时崩溃
                // 如果设置了ViewPager.offscreenPageLimits = adapter.count时这里不会走的，如果没有设置，这里就是一道保险
                (it as ViewGroup).removeAllViews()
            }
            baseBinding.mClBaseContent.addView(viewBinding.root, params)
        }
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    open fun initView(rootView: View) {}

    abstract fun initAction()

    private fun initEvent() {
        onRegisterEvent()?.let {
            LiveEventBus
                .get(it, Any::class.java)
                .observe(this, Observer { data: Any? ->
                    try {
                        onReceiveEvent(data)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                })
        }
    }

    protected open fun onRegisterEvent(): String? = null

    protected open fun onReceiveEvent(data: Any?) {}

    abstract fun sendRequest()

    override fun getCurrentActivity() = mActivity ?: mContext as Activity

    override fun logBackIn() {
        // 重新打开登录界面
    }

    override fun showToast(msg: String?) {
        msg.toast()
    }

    override fun showLoadingView(isCanNotCancel: Boolean) {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog.newInstance()
        } else {
            mLoadingDialog?.dismissAllowingStateLoss()
        }
        mLoadingDialog?.canNotCancel = isCanNotCancel
        mActivity?.let {
            mLoadingDialog?.show(
                it.supportFragmentManager,
                LoadingDialog::class.java.simpleName
            )
        }
    }

    override fun hideLoadingView() {
        mLoadingDialog?.dismissAllowingStateLoss()
    }

    override fun onFailed(action: Int, errorCode: Int, message: String) {
    }

    protected inline fun <reified T : ViewBinding> bindingView(): Lazy<T> =
        lazy(LazyThreadSafetyMode.NONE) {
            val viewBindClass = T::class.java
            val method = viewBindClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
            return@lazy method.invoke(null, layoutInflater) as T
        }

}