package com.william.base_component.activity


import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewbinding.ViewBinding
import com.william.base_component.R
import com.william.base_component.databinding.BaseActivityBinding
import com.william.base_component.manager.StatusBarManager


/**
 * @author William
 * @date 2020/4/16 16:09
 * Class Comment：base activity
 */
abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var mActivity: Activity

    private lateinit var mIvBaseTitleLeft: ImageView
    private lateinit var mTvBaseTitleText: TextView
    private lateinit var mIvBaseTitleRight: ImageView
    private lateinit var mTvBaseTitleRight: TextView
    private lateinit var mViewBaseLine: View

    private lateinit var mBaseBinding: BaseActivityBinding
    protected abstract val mViewBinding: ViewBinding

    @get:LayoutRes
    protected abstract val layoutId: Int

    protected open val darkMode: Boolean = true

    protected open val showTitleBar: Boolean = true

    protected open val setFitSystemWindow: Boolean = true

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
        StatusBarManager.setStatusBar(this, darkMode)
    }

    private fun initLayout() {
        if (showTitleBar) {
            mBaseBinding.vsBaseTitle.inflate()
            mIvBaseTitleLeft = findViewById(R.id.iv_baseTitleLeft)
            mTvBaseTitleText = findViewById(R.id.tv_baseTitleText)
            mIvBaseTitleRight = findViewById(R.id.iv_baseTitleRight)
            mTvBaseTitleRight = findViewById(R.id.tv_baseTitleRight)
            mViewBaseLine = findViewById(R.id.view_baseLine)
            initTitleBarListener()
        }
        if (layoutId != 0) {
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
            mBaseBinding.clBaseContent.addView(mViewBinding.root, params)
        }
        mBaseBinding.clBaseContent.fitsSystemWindows = setFitSystemWindow
    }

    abstract fun initView()

    abstract fun initAction()

    abstract fun initData()

    private fun initEvent() {
    }

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

}
