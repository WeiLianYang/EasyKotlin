package com.william.base_component.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.william.base_component.BaseApp
import com.william.base_component.R

/**
 * @author William
 * @date 2020/5/21 19:45
 * Class Comment：custom footer
 */
class CustomClassicsFooter : ClassicsFooter {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    /**
     * 设置数据全部加载完成，将不能再次触发加载功能
     */
    override fun setNoMoreData(noMoreData: Boolean): Boolean {
        if (mNoMoreData != noMoreData) {
            mNoMoreData = noMoreData
            val arrowView: View = mArrowView
            if (noMoreData) {
                mTitleText.text = "已经到底了～"
                arrowView.visibility = View.GONE
            } else {
                mTitleText.text = mTextPulling
                mTitleText.background = null
                mTitleText.setTextColor(
                    BaseApp.instance.getColor(R.color.color_232323)
                )
                arrowView.visibility = View.VISIBLE
            }
        }
        return true
    }
}