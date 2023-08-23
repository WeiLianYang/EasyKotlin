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

import androidx.lifecycle.lifecycleScope
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.animatedTransformation
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import coil.transition.CrossfadeTransition
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.base_component.extension.dp
import com.william.base_component.extension.logD
import com.william.base_component.extension.logE
import com.william.base_component.extension.logI
import com.william.base_component.image.CircleAnimatedTransformation
import com.william.base_component.image.RoundedAnimatedTransformation
import com.william.easykt.R
import com.william.easykt.databinding.ActivityCoilBinding
import kotlinx.coroutines.launch

/**
 * ## author : WilliamYang
 * ## date : 2022/11/1 9:47
 * ## description : [Coil document](https://coil-kt.github.io/coil/getting_started/)
 */
class CoilActivity : BaseActivity() {

    override val viewBinding: ActivityCoilBinding by bindingView()

    private val url = "https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF"

    private val gifUrl1 =
        "https://s1.aigei.com/src/img/gif/a2/a23e1935964d4465bcfe1f17625fc8a6.gif?imageMogr2/auto-orient/thumbnail/!240x225r/gravity/Center/crop/240x225/quality/85/&e=1735488000&token=P7S2Xpzfz11vAkASLTkfHN7Fw-oOZBecqeJaxypL:jwsq63qB0OOfuMOr-KKnCye7ln4="
    private val gifUrl2 = "https://img.zcool.cn/community/0193385d7f8cada801211d532d307b.gif"


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

        val request = ImageRequest.Builder(this)
            .data(url)
            .lifecycle(this)
            .transformations(RoundedCornersTransformation(20f.dp))
            .target(viewBinding.ivImage10)
            .build()
        imageLoader.enqueue(request)
//        val disposable = imageLoader.enqueue(request)
//        disposable.dispose()

        val request2 = ImageRequest.Builder(this)
            .data(url)
            .transformations(CircleCropTransformation())
            .build()
        lifecycleScope.launch {
            val drawable = imageLoader.execute(request2).drawable
            viewBinding.ivImage11.setImageDrawable(drawable)
        }

        val request3 = ImageRequest.Builder(this)
            .data(url)
            .transitionFactory(CrossfadeTransition.Factory())
            .allowConversionToBitmap(false)
            .target(
                onStart = { placeholder ->
                    // Handle the placeholder drawable.
                    "Image load onStart()...".logD()
                },
                onSuccess = { result ->
                    // Handle the successful result.
                    "Image load onSuccess()...".logI()
                },
                onError = { error ->
                    // Handle the error drawable.
                    "Image load onError()...".logE()
                }
            )
            .build()
        imageLoader.enqueue(request3)

        viewBinding.ivImage12.load(gifUrl1) {
            crossfade(true)
            placeholder(R.drawable.ic_default)
            lifecycle(this@CoilActivity)
            @OptIn(ExperimentalCoilApi::class)
            animatedTransformation(RoundedAnimatedTransformation(20f.dp, 0f, 0f, 20f.dp))
        }

        viewBinding.ivImage13.load(gifUrl2) {
            placeholder(R.drawable.ic_default)
            lifecycle(this@CoilActivity)
            @OptIn(ExperimentalCoilApi::class)
            animatedTransformation(CircleAnimatedTransformation())
        }

    }

    override fun initData() {
        setTitleText(R.string.test_coil)
    }

    override fun initAction() {

    }

}