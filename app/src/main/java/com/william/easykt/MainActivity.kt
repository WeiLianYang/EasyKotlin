/*
 * Copyright WeiLianYang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.william.easykt

import com.william.base_component.activity.BaseActivity
import com.william.base_component.utils.openActivity
import com.william.base_component.utils.openBrowser
import com.william.easykt.databinding.ActivityMainBinding
import com.william.easykt.test.TestActivity
import com.william.easykt.ui.AutoScrollActivity
import com.william.easykt.ui.PagerCardActivity
import com.william.easykt.ui.SwipeCardActivity
import com.william.easykt.ui.WaveAnimationActivity

/**
 * @author William
 * @date 4/10/21 7:23 PM
 * Class Comment：主入口
 */
class MainActivity : BaseActivity() {
    override val mViewBinding: ActivityMainBinding by bindingView()

    override fun initAction() {

        mViewBinding.apply {
            tvButton1.setOnClickListener {
                openActivity<TestActivity>(mActivity) {
                    putExtra("name", "Stark")
                }
            }

            tvButton2.setOnClickListener {
                openActivity<SwipeCardActivity>(mActivity)
            }

            tvButton3.setOnClickListener {
                openActivity<WaveAnimationActivity>(mActivity)
            }

            tvButton4.setOnClickListener {
                openActivity<PagerCardActivity>(mActivity)
            }

            tvButton5.setOnClickListener {
                openActivity<AutoScrollActivity>(mActivity)
            }

            tvButton7.setOnClickListener {
                openBrowser(
                    mActivity,
                    "https://www.baidu.com",
                    "com.tencent.mtt",
                    "com.tencent.mtt.MainActivity"
                )
            }
        }
    }

    override fun initData() {
        setTitleText("EasyKotlin")
    }

}