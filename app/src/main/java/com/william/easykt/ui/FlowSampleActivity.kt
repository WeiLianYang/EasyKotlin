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
import com.william.easykt.databinding.ActivityFlowSampleBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * author : WilliamYang
 * date : 2021/7/30 10:41
 * description : Kotlin flow demo
 */
class FlowSampleActivity : BaseActivity() {

    override val mViewBinding: ActivityFlowSampleBinding by bindingView()

    override fun initAction() {
        mViewBinding.apply {
            tvButton1.setOnClickListener {
                sample1()
            }

            tvButton2.setOnClickListener {

            }

            tvButton3.setOnClickListener {

            }

            tvButton4.setOnClickListener {

            }

            tvButton5.setOnClickListener {

            }

            tvButton6.setOnClickListener {

            }

            tvButton7.setOnClickListener {

            }
        }
    }

    private fun numberFlow(): Flow<Int> = flow { // 流构建器
        for (i in 1..3) {
            delay(100) // 假装我们在这里做了一些有用的事情
            emit(i) // 发送下一个值
        }
    }

    private fun sample1() = runBlocking {
        // 启动并发的协程以验证主线程并未阻塞
        launch {
            for (k in 1..3) {
                println("I'm not blocked $k")
                delay(100)
            }
            // 收集这个流
        }
        numberFlow().collect { value -> println(value) }
    }

    override fun initData() {
        setTitleText("FlowSample")
    }
}