package com.william.base_component.activity


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.william.base_component.R
import com.william.base_component.fragment.LoadingDialog
import com.william.base_component.manager.StatusBarManager
import com.william.base_component.mvp.base.IBaseView


/**
 * @author William
 * @date 2020/4/16 16:09
 * Class Comment：base activity
 */
abstract class BaseActivity : AppCompatActivity(), IBaseView {

    private lateinit var mContext: Context
    protected lateinit var mActivity: Activity

    private lateinit var mClBaseContent: ConstraintLayout
    private lateinit var mVsBaseTitle: ViewStub
    private lateinit var mIvBaseTitleLeft: ImageView
    private lateinit var mTvBaseTitleText: TextView
    private lateinit var mIvBaseTitleRight: ImageView
    private lateinit var mTvBaseTitleRight: TextView
    private lateinit var mViewBaseLine: View
    private var mLoadingDialog: LoadingDialog? = null

    @get:LayoutRes
    protected abstract val layoutId: Int

    protected open val needSetStatusBar: Boolean = true

    protected open val darkMode: Boolean = true

    protected open val showTitleBar: Boolean = true

    protected open val setFitSystemWindow: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)

        mActivity = this
        mContext = applicationContext

        initStatusBar()
        initLayout()
        initView()
        initListener()
        initData()
        initEvent()
    }

    private fun initStatusBar() {
        if (needSetStatusBar) {
            StatusBarManager.setStatusBar(this, darkMode)
        }
    }

    private fun initLayout() {
        mClBaseContent = findViewById(R.id.cl_baseContent)
        mVsBaseTitle = findViewById(R.id.vs_baseTitle)
        if (showTitleBar) {
            mVsBaseTitle.inflate()
            mIvBaseTitleLeft = findViewById(R.id.iv_baseTitleLeft)
            mTvBaseTitleText = findViewById(R.id.tv_baseTitleText)
            mIvBaseTitleRight = findViewById(R.id.iv_baseTitleRight)
            mTvBaseTitleRight = findViewById(R.id.tv_baseTitleRight)
            mViewBaseLine = findViewById(R.id.view_baseLine)
            initTitleBarListener()
        }
        mClBaseContent.fitsSystemWindows = setFitSystemWindow

        initContent()
    }

    abstract fun initView()

    abstract fun initListener()

    abstract fun initData()

    private fun initEvent() {
    }

    private fun initContent() {
        val contentView: View?
        if (layoutId != 0) {
            contentView = LayoutInflater.from(this).inflate(layoutId, mClBaseContent, false)
            val params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
            )
            if (showTitleBar) {
                params.topToBottom = R.id.cl_baseTitle
            } else {
                params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            }
            params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            mClBaseContent.addView(contentView, params)
        }
    }

    private fun initTitleBarListener() {
        mIvBaseTitleLeft.setOnClickListener { view: View? ->
            onBackPressed(view)
        }
        mIvBaseTitleRight.setOnClickListener { view: View? ->
            onRightIconClickListener(view)
        }
        mTvBaseTitleRight.setOnClickListener { view: View? ->
            onRightTextClickListener(view)
        }
    }

    fun onBackPressed(v: View?) {
        onBackPressed()
    }

    protected open fun onRightIconClickListener(view: View?) {}

    protected open fun onRightTextClickListener(view: View?) {}

    protected fun setTitleText(@StringRes stringId: Int) {
        mTvBaseTitleText.text = resources.getText(stringId)
    }

    override fun onDestroy() {
        cancelLoading()
        super.onDestroy()
    }

    companion object {

        const val REQUEST_CODE_SETTING = 1

    }

    override fun getCurrentActivity(): Activity = mActivity

    override fun showToast(msg: String) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show()
    }

    override fun startLoading(isCanNotCancel: Boolean) {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog()
        } else {
            mLoadingDialog?.dismissAllowingStateLoss()
        }
        mLoadingDialog!!.canNotCancel = isCanNotCancel
        if (mLoadingDialog!!.isAdded) {
            mLoadingDialog!!.show(
                supportFragmentManager,
                LoadingDialog::class.java.simpleName
            )
        }
    }

    override fun cancelLoading() {
        mLoadingDialog?.dismissAllowingStateLoss()
    }

    override fun onFailed(action: Int, errorCode: Int, message: String) {
    }

}

fun main() {
    val str: String? = "Hello"
    val length = str?.let {
        println("let() called on $it")
//        processNonNullString(it)      // 编译通过：'it' 在 '?.let { }' 中必不为空
        it.length
    }
}