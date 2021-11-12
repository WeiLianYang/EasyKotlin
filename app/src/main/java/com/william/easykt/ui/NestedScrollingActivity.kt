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
import com.william.base_component.extension.bindingView
import com.william.easykt.databinding.ActivityNestedScrollingBinding
import com.william.easykt.ui.adapter.SimpleAdapter

/**
 * author : WilliamYang
 * date : 2021/7/30 10:41
 * description : 嵌套滑动
 */
class NestedScrollingActivity : BaseActivity() {

    override val viewBinding: ActivityNestedScrollingBinding by bindingView()

    override fun initAction() {
    }

    override fun initData() {
        setTitleText("NestedScrollingLayout")
        viewBinding.recyclerView.adapter = SimpleAdapter()
    }
}