package com.william.easykt.test

import android.content.Context
import android.content.Intent
import com.william.base_component.mvp.BaseMvpActivity
import com.william.easykt.R
import com.william.easykt.data.Banner
import com.william.easykt.data.HttpResponse
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
        setTitleText(R.string.album_title)

        mPresenter?.getBanners()
    }

    override val mViewBinding: ActivityTestBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityTestBinding.inflate(layoutInflater)
    }

    override val layoutId: Int
        get() = R.layout.activity_test

    override fun setupData(response: HttpResponse<List<Banner>>?) {
        mViewBinding.tvText.text = response?.toString()
        mViewBinding.ivTestImage.setImageResource(R.mipmap.ic_launcher)
        mViewBinding.includeLayout.ivIncludeImage.setImageResource(R.mipmap.ic_launcher_round)
    }

    companion object {

        @JvmStatic
        fun startTarget(context: Context) {
            val intent = Intent(context, TestActivity::class.java)
            context.startActivity(intent)
        }
    }
}