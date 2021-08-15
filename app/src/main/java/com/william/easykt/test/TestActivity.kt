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
import com.william.easykt.databinding.ActivityTestBinding
import com.william.easykt.service.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking


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
        setTitleText("TestActivity")
//        mPresenter.getBanners()

        runBlocking {
            flow {
                val response = Api.apiService.getTopArticles()
                emit(response.data)
            }.flowOn(Dispatchers.IO)
                .onCompletion { cause -> if (cause != null) println("Flow completed exceptionally") } // 2 观察上游异常
                .catch { e -> println("Caught exception: $e") } // 3 需要放在 onCompletion 之后，否则 onCompletion 不执行
                .collect { value ->
                    setupData(value.toString()) // 1
                }
        }
    }

    override val mViewBinding: ActivityTestBinding by bindingView()

    override fun setupData(response: String?) {
        mViewBinding.tvText.text = response
        mViewBinding.ivTestImage.setImageResource(R.mipmap.ic_launcher)
        mViewBinding.includeLayout.ivIncludeImage.setImageResource(R.mipmap.ic_launcher_round)
    }

}