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

    override fun setupData(response: List<Banner>?) {
        mViewBinding.tvText.text = response?.toString()
        mViewBinding.ivTestImage.setImageResource(R.mipmap.ic_launcher)
        mViewBinding.includeLayout.ivIncludeImage.setImageResource(R.mipmap.ic_launcher_round)
    }

}