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

    override val viewBinding: ActivityPagerCardBinding by bindingView()

    private lateinit var cardAdapter: PagerCardAdapter

    override fun initAction() {
    }

    override fun initData() {
        setTitleText("PagerCardActivity")
        cardAdapter = PagerCardAdapter()
        viewBinding.viewPager.apply {
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