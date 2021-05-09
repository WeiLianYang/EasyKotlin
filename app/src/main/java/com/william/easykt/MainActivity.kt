package com.william.easykt

import com.william.base_component.activity.BaseActivity
import com.william.base_component.utils.openActivity
import com.william.easykt.databinding.ActivityMainBinding
import com.william.easykt.test.TestActivity
import com.william.easykt.ui.AutoScrollActivity
import com.william.easykt.ui.PagerCardActivity
import com.william.easykt.ui.SwipeCardActivity
import com.william.easykt.ui.WaveAnimationActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author William
 * @date 4/10/21 7:23 PM
 * Class Comment：主入口
 */
class MainActivity : BaseActivity() {
    override val mViewBinding: ActivityMainBinding by bindingView()

    override fun initAction() {

        mViewBinding.apply {
            tv_button1.setOnClickListener {
                openActivity<TestActivity>(mActivity) {
                    putExtra("name", "Stark")
                }
            }

            tv_button2.setOnClickListener {
                openActivity<SwipeCardActivity>(mActivity)
            }

            tv_button3.setOnClickListener {
                openActivity<WaveAnimationActivity>(mActivity)
            }

            tv_button4.setOnClickListener {
                openActivity<PagerCardActivity>(mActivity)
            }

            tv_button5.setOnClickListener {
                openActivity<AutoScrollActivity>(mActivity)
            }
        }
    }

    override fun initData() {
        setTitleText("EasyKotlin")
    }

}