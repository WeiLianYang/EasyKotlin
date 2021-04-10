package com.william.easykt.ui

import com.william.base_component.activity.BaseActivity
import com.william.easykt.databinding.ActivitySwipeCardBinding
import com.william.easykt.ui.adapter.SwipeCardAdapter


/**
 * @author William
 * @date 2020/6/28 14:03
 * Class Comment：
 */
class SwipeCardActivity : BaseActivity() {

    override val mViewBinding: ActivitySwipeCardBinding by bindingView()

    private lateinit var cardAdapter: SwipeCardAdapter

    override fun initAction() {
    }

    override fun initData() {
        setTitleText("SwipeCardActivity")
        cardAdapter = SwipeCardAdapter()
        mViewBinding.viewPager.apply {
            offscreenPageLimit = 3
            adapter = cardAdapter
            setPageTransformer(
                true,
                CascadingCardPageTransformer(this)
            )
        }
    }
}