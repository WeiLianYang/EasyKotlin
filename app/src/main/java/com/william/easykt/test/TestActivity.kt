package com.william.easykt.test

import com.william.base_component.mvp.BaseMvpActivity
import com.william.easykt.R
import com.william.easykt.data.Banner
import com.william.easykt.databinding.ActivityTestBinding


/**
 * @author William
 * @date 2020/5/3 15:49
 * Class Comment：
 */
class TestActivity : BaseMvpActivity<TestPresenter>(), TestContract.IView {

    override fun initView() {
    }

    override fun initAction() {
    }

    override fun initData() {

        mPresenter.getBanners()
    }

    override val mViewBinding: ActivityTestBinding by bindingView()

    override fun getLayoutId(): Int = R.layout.activity_test

    override fun setupData(response: List<Banner>?) {
        mViewBinding.tvText.text = response?.toString()
        mViewBinding.ivTestImage.setImageResource(R.mipmap.ic_launcher)
        mViewBinding.includeLayout.ivIncludeImage.setImageResource(R.mipmap.ic_launcher_round)
    }

}