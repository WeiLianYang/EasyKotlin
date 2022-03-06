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

package com.william.easykt.test

import androidx.activity.viewModels
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.easykt.databinding.ActivityTestBinding


/**
 * @author William
 * @date 2020/5/3 15:49
 * Class Comment：
 */
class TestVmActivity : BaseActivity() {

    override val viewBinding: ActivityTestBinding by bindingView()

    private val viewModel by viewModels<TestViewModel> {
        TestViewModel.provideFactory(TestRepository())
    }

    override fun initView() {
    }

    override fun initAction() {
    }

    override fun initData() {
        setTitleText("TestVmActivity")

        viewModel.testData.observe(this) {
            viewBinding.tvText.text = it.toString()
        }
        viewModel.requestData()
    }


}