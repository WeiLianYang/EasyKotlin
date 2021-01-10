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
    protected lateinit var mBaseBinding: BaseFragmentBinding
    protected abstract val mViewBinding: ViewBinding

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
        mBaseBinding = BaseFragmentBinding.inflate(layoutInflater)
        val rootView = mBaseBinding.root
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
            mViewBinding.root.parent?.let {
                // 因为mViewBinding只被初始化一次，下次访问时还是上次的值，所以需要将它的父View和其解除关系，避免被重新addView时崩溃
                // 如果设置了ViewPager.offscreenPageLimits = adapter.count时这里不会走的，如果没有设置，这里就是一道保险
                (it as ViewGroup).removeAllViews()
            }
            mBaseBinding.mClBaseContent.addView(mViewBinding.root, params)
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