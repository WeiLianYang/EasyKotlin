package com.william.base_component.activity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
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


/**
 * @author William
 * @date 2020/4/16 16:09
 * Class Comment：base activity
 */
abstract class BaseActivity : AppCompatActivity(), IBaseView {

    protected lateinit var mActivity: FragmentActivity

    private lateinit var mIvBaseTitleLeft: ImageView
    private lateinit var mTvBaseTitleText: TextView
    private lateinit var mIvBaseTitleRight: ImageView
    private lateinit var mTvBaseTitleRight: TextView
    private lateinit var mViewBaseLine: View

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
            mIvBaseTitleLeft = findViewById(R.id.iv_baseTitleLeft)
            mTvBaseTitleText = findViewById(R.id.tv_baseTitleText)
            mIvBaseTitleRight = findViewById(R.id.iv_baseTitleRight)
            mTvBaseTitleRight = findViewById(R.id.tv_baseTitleRight)
            mViewBaseLine = findViewById(R.id.view_baseLine)
            initTitleBarListener()
        }
        if (getLayoutId() != 0) {
            val params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
            )
            if (isShowTitleBar()) {
                params.topToBottom = R.id.cl_baseTitle
            } else {
                params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            }
            params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            mBaseBinding.mClBaseContent.addView(mViewBinding.root, params)
        }
        mBaseBinding.mClBaseContent.fitsSystemWindows = isFitSystemWindow()
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    open fun initView() {}

    abstract fun initAction()

    abstract fun initData()

    private fun initEvent() {
        onRegisterEvent()?.let {
            LiveEventBus
                .get(it, Any::class.java)
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
        mIvBaseTitleLeft.setOnClickListener {
            onBackPressed()
        }
        mIvBaseTitleRight.setOnClickListener {
            onRightIconClickListener(it)
        }
        mTvBaseTitleRight.setOnClickListener {
            onRightTextClickListener(it)
        }
    }

    protected open fun onRightIconClickListener(view: View?) {}

    protected open fun onRightTextClickListener(view: View?) {}

    protected fun setTitleText(@StringRes stringId: Int) {
        mTvBaseTitleText.text = resources.getText(stringId)
    }

    protected fun setTitleText(title: String) {
        mTvBaseTitleText.text = title
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
