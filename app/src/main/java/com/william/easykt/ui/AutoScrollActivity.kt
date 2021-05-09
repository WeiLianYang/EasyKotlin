package com.william.easykt.ui

import com.william.base_component.activity.BaseActivity
import com.william.easykt.databinding.ActivityAutoScrollBinding
import com.william.easykt.ui.scroll.addScrollImageView


/**
 * @author WilliamYang
 * @date 2021/2/19 13:22
 * Class Comment：自动滑动
 */
class AutoScrollActivity : BaseActivity() {

    override val mViewBinding: ActivityAutoScrollBinding by bindingView()

    override fun initAction() {
    }

    override fun initData() {
        setTitleText("AutoScrollActivity")

        addScrollImageView(mViewBinding.flContent)
    }
}