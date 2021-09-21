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

import androidx.activity.viewModels
import com.william.base_component.activity.BaseActivity
import com.william.base_component.utils.toPx
import com.william.easykt.R
import com.william.easykt.databinding.ActivityCoroutinesSampleBinding
import com.william.easykt.ui.adapter.UsageAdapter
import com.william.easykt.viewmodel.SampleViewModel
import com.zyyoona7.itemdecoration.RecyclerViewDivider
import kotlinx.coroutines.*

/**
 * author：William
 * date：2021/9/21 22:27
 * description：coroutine sample
 * @see <a href="http://www.kotlincn.net/docs/reference/coroutines/coroutines-guide.html">协程指南</a>
 */
class CoroutineSampleActivity : BaseActivity() {

    override val viewBinding: ActivityCoroutinesSampleBinding by bindingView()
    private lateinit var mAdapter: UsageAdapter
    private val viewModel by viewModels<SampleViewModel>()

    override fun initView() {
        super.initView()
        viewBinding.recyclerView.apply {
            adapter = UsageAdapter().also { mAdapter = it }
            RecyclerViewDivider.staggeredGrid()
                .includeStartEdge()
                .includeEdge().spacingSize(R.dimen.dp_10.toPx())
                .build().addTo(this)
        }
    }

    override fun initData() {
        setTitleText("Coroutines Sample")
    }

    @DelicateCoroutinesApi
    override fun initAction() {
        viewModel.coroutinesSampleData.observe(this, {
            mAdapter.setList(it)
        })

        mAdapter.setOnItemClickListener { _, position, _ ->
            when (position) {
                0 -> sample0()
                1 -> sample1()
                2 -> sample2()
                3 -> sample3()
                4 -> sample4()
                5 -> sample5()
                6 -> sample6()
                7 -> sample7()
                else -> {
                }
            }
        }
    }

    @DelicateCoroutinesApi
    private fun sample0() = runBlocking { // 开始执行主协程，runBlocking会阻塞当前线程直到协程停止
        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L)
            println("Coroutines!")
        }
        println("Hello,") // 主协程在这里会立即执行
        delay(2000L) // 延迟 2 秒来保证 JVM 存活
    }

    /**
     * 对 sample0() 进行优化
     */
    @DelicateCoroutinesApi
    private fun sample1() = runBlocking {
        val job = GlobalScope.launch { // 启动一个新协程并保持对这个作业的引用
            delay(1000L)
            println("Coroutines!")
        }
        println("Hello,")
        job.join() // 等待直到子协程执行结束
    }

    /**
     * 结构化的并发，对 sample1() 进行简化
     * 外部协程（示例中的 runBlocking）直到在其作用域中启动的所有协程都执行完毕后才会结束
     */
    private fun sample2() = runBlocking {
        launch { // 在 runBlocking 作用域中启动一个新协程
            delay(1000L)
            println("Coroutines!")
        }
        println("Hello,")
    }

    private fun sample3() {

    }

    private fun sample4() {

    }

    private fun sample5() {

    }

    private fun sample6() {

    }

    private fun sample7() {

    }
}
