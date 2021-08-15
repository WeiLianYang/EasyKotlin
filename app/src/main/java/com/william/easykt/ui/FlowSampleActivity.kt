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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

/**
 * author : WilliamYang
 * date : 2021/7/30 10:41
 * description : Kotlin flow demo
 */
class FlowSampleActivity : BaseActivity() {

    override val mViewBinding: ActivityFlowSampleBinding by bindingView()

    override fun initData() {
        setTitleText("FlowSample")
    }

    override fun initAction() {
        mViewBinding.apply {
            tvButton1.setOnClickListener {
                sample1()
            }

            tvButton2.setOnClickListener {
                sample2()
            }

            tvButton3.setOnClickListener {
                sample3()
            }

            tvButton4.setOnClickListener {
                sample4()
            }

            tvButton5.setOnClickListener {
                sample5()
            }

            tvButton6.setOnClickListener {

            }

            tvButton7.setOnClickListener {

            }
        }
    }

    /**
     * 构建流，并且方法不需要标记为suspend
     * 被调用后，会尽快返回且不会进行任何等待，该流在每次收集的时候启动
     */
    private fun numberFlow(): Flow<Int> = flow { // 流构建器
        for (i in 1..3) {
            println("Flow started")
            delay(100) // 假装我们在这里做了一些有用的事情
            emit(i) // 发送下一个值
        }
    }

    /**
     * usage 1 : 异步计算的值流
     * 名为 flow 的 Flow 类型构建器函数。
     * flow { ... } 构建块中的代码可以挂起。
     * 流使用 emit 函数 发射 值。
     * 流使用 collect 函数 收集 值。
     */
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

    /**
     * usage 2 : 冷流
     * Flow 是一种类似于序列的冷流，flow 构建器中的代码直到流被收集的时候才运行
     */
    private fun sample2() = runBlocking {
        println("Calling simple function...")
        val flow = numberFlow()
        println("Calling collect...")
        flow.collect { value -> println(value) }
        println("Calling collect again...")
        flow.collect { value -> println(value) }
    }

    /**
     * usage 3 : 取消 流
     * 流采用与协程同样的协作取消。像往常一样，流的收集可以在当流在一个可取消的挂起函数（例如 delay）中挂起的时候取消
     */
    private fun sample3() = runBlocking {
        withTimeoutOrNull(250) {
            // 在 250 毫秒后超时，超时后流将被取消
            numberFlow().collect { value -> println(value) }
        }
        println("Done")
    }

    /**
     * usage 4 :
     * 1. 使用 .asFlow() 扩展函数，可以将各种集合与序列转换为流。
     * 2. flowOf 构建器定义了一个发射固定值集的流。
     */
    private fun sample4() = runBlocking {
        // 将一个整数区间转化为流
        (1..3).asFlow().collect { value -> println(value) }
//        flowOf(1, 2, 3).collect { value -> println(value) }
    }

    /**
     * usage 5 : 可以使用操作符转换流，就像使用集合与序列一样。
     * 1. 过渡操作符应用于上游流，并返回下游流。这些操作符也是冷操作符，就像流一样。
     * 2. 这类操作符本身不是挂起函数。它运行的速度很快，返回新的转换流的定义。
     * 3. 基础的操作符拥有相似的名字，比如 map 与 filter。
     * 4. 流与序列的主要区别在于这些操作符中的代码可以调用挂起函数。
     */
    private fun sample5() = runBlocking {
        (1..3).asFlow() // 一个请求流
            .map { request ->
                performRequest(request) // Int 被转换为String
            }
//            .filter { it > 1 }
            .collect { response -> println(response) }
    }

    private suspend fun performRequest(request: Int): String {
        delay(1000) // 模仿长时间运行的异步工作
        return "performRequest-response: $request"
    }

}