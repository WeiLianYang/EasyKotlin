package com.william.easykt.ui

import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.dp
import com.william.easykt.databinding.ActivityPagerCardBinding
import com.william.easykt.ui.adapter.PagerCardAdapter


/**
 * @author WilliamYang
 * @date 2021/2/19 13:22
 * Class Comment：卡片轮播
 */
class PagerCardActivity : BaseActivity() {

    override val mViewBinding: ActivityPagerCardBinding by bindingView()

    private lateinit var cardAdapter: PagerCardAdapter

    override fun initAction() {
    }

    override fun initData() {
        setTitleText("PagerCardActivity")
        cardAdapter = PagerCardAdapter()
        mViewBinding.viewPager.apply {
            pageMargin = 18.dp
            offscreenPageLimit = 3
            adapter = cardAdapter
            setPageTransformer(
                true,
                PagerCardTransformer()
            )
            currentItem = Int.MAX_VALUE / 2
        }
    }
}