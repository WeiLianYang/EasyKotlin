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

import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.base_component.extension.dp
import com.william.easykt.R
import com.william.easykt.databinding.ActivityCoilBinding

/**
 * author : WilliamYang
 * date : 2022/11/1 9:47
 * description : Coil
 */
class CoilActivity : BaseActivity() {

    override val viewBinding: ActivityCoilBinding by bindingView()

    private val url = "https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF"

    override fun initView() {
        super.initView()

        // URL
        viewBinding.ivImage1.load(url)

        // drawable
        viewBinding.ivImage2.load(R.drawable.fish)

        // raw
        viewBinding.ivImage3.load(R.raw.chick2)

        viewBinding.ivImage4.load(R.drawable.temple) {
            crossfade(true)
            placeholder(R.drawable.ic_default)
            lifecycle(this@CoilActivity)
            transformations(CircleCropTransformation())
        }

        viewBinding.ivImage5.load(R.drawable.chick) {
            crossfade(true)
            placeholder(R.drawable.ic_default)
            lifecycle(this@CoilActivity)
            transformations(RoundedCornersTransformation(20f.dp))
        }

        viewBinding.ivImage6.load(R.drawable.fish) {
            crossfade(true)
            placeholder(R.drawable.ic_default)
            lifecycle(this@CoilActivity)
            transformations(RoundedCornersTransformation(20f.dp, 20f.dp))
        }

        viewBinding.ivImage7.load(url) {
            crossfade(true)
            placeholder(R.drawable.ic_default)
            lifecycle(this@CoilActivity)
            transformations(RoundedCornersTransformation(20f.dp, 20f.dp, 20f.dp))
        }

        viewBinding.ivImage8.load(R.drawable.temple) {
            crossfade(true)
            placeholder(R.drawable.ic_default)
            lifecycle(this@CoilActivity)
            transformations(RoundedCornersTransformation(20f.dp, 20f.dp, 20f.dp, 20f.dp))
        }

        viewBinding.ivImage9.load(url) {
            crossfade(true)
            placeholder(R.drawable.ic_default)
            lifecycle(this@CoilActivity)
            transformations(CircleCropTransformation())
        }
    }

    override fun initData() {
        setTitleText(R.string.test_coil)
    }

    override fun initAction() {

    }

}