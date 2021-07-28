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

package com.william.base_component.activity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.jeremyliao.liveeventbus.LiveEventBus
import com.william.base_component.R
import com.william.base_component.databinding.BaseActivityBinding
import com.william.base_component.extension.toast
import com.william.base_component.fragment.LoadingDialog
import com.william.base_component.manager.setStatusBar
import com.william.base_component.mvp.base.IBaseView
import com.william.base_component.widgets.TitleBarLayout


/**
 * @author William
 * @date 2020/4/16 16:09
 * Class Comment：base activity
 */
abstract class BaseActivity : AppCompatActivity(), IBaseView {

    protected lateinit var mActivity: FragmentActivity

    private lateinit var titleBarLayout: TitleBarLayout

    private var mLoadingDialog: LoadingDialog? = null
    private lateinit var mBaseBinding: BaseActivityBinding

    /**
     * Implemented by subclasses using standard delegates, for example:
     * override val mViewBinding: ActivityMainBinding by bindingView()
     */
    protected abstract val mViewBinding: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBaseBinding = BaseActivityBinding.inflate(layoutInflater)
        setContentView(mBaseBinding.root)

        mActivity = this

        initStatusBar()
        initLayout()
        initView()
        initAction()
        initData()
        initEvent()
    }

    protected open fun initStatusBar() {
        setStatusBar(this, isDarkMode())
    }

    private fun initLayout() {
        if (isShowTitleBar()) {
            mBaseBinding.vsBaseTitle.inflate()
            titleBarLayout = findViewById(R.id.titleLayout)
            initTitleBarListener()
        }

        val params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
        )
        if (isShowTitleBar()) {
            params.topToBottom = R.id.titleLayout
        } else {
            params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        }
        params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        mBaseBinding.mClBaseContent.addView(mViewBinding.root, params)

        mBaseBinding.mClBaseContent.fitsSystemWindows = isFitSystemWindow()
    }

    open fun initView() {}

    abstract fun initAction()

    abstract fun initData()

    private fun initEvent() {
        onRegisterEvent()?.let {
            LiveEventBus
                .get(it)
                .observe(this, { data: Any? ->
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

    protected open fun isDarkMode(): Boolean = true

    protected open fun isShowTitleBar(): Boolean = true

    protected open fun isFitSystemWindow(): Boolean = true

    private fun initTitleBarListener() {
        titleBarLayout.apply {
            leftIconClickListener = { onBackPressed() }
            rightIconClickListener = { onRightIconClickListener(it) }
            rightTextClickListener = { onRightTextClickListener(it) }
        }
    }

    protected open fun onRightIconClickListener(view: View?) {}

    protected open fun onRightTextClickListener(view: View?) {}

    protected fun setTitleText(@StringRes stringId: Int) {
        setTitleText(resources.getText(stringId).toString())
    }

    protected fun setTitleText(title: String) {
        titleBarLayout.setTitle(title)
    }

    fun setTitleLeftImageRes(resId: Int) {
        titleBarLayout.setLeftImageRes(resId)
    }

    fun setTitleRightImageRes(resId: Int) {
        titleBarLayout.setRightImageRes(resId)
    }

    fun setTitleRightText(text: String?) {
        titleBarLayout.setRightText(text)
    }

    fun setTitleLineVisible(visible: Boolean) {
        titleBarLayout.setLineVisible(visible)
    }

    override fun getCurrentActivity() = mActivity

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
        mLoadingDialog?.show(supportFragmentManager, LoadingDialog::class.java.simpleName)
    }

    override fun hideLoadingView() {
        mLoadingDialog?.dismissAllowingStateLoss()
    }

    override fun onFailed(action: Int, errorCode: Int, message: String) {
    }

    override fun onDestroy() {
        hideLoadingView()
        super.onDestroy()
    }

    protected inline fun <reified T : ViewBinding> bindingView(): Lazy<T> =
        lazy(LazyThreadSafetyMode.NONE) {
            val viewBindClass = T::class.java
            val method = viewBindClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
            return@lazy method.invoke(null, layoutInflater) as T
        }

}
