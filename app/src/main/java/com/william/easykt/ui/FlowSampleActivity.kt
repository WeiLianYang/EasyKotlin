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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

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
            tvButton1.setOnClickListener { sample1() }
            tvButton2.setOnClickListener { sample2() }
            tvButton3.setOnClickListener { sample3() }
            tvButton4.setOnClickListener { sample4() }
            tvButton5.setOnClickListener { sample5() }
            tvButton6.setOnClickListener { sample6() }
            tvButton7.setOnClickListener { sample7() }
            tvButton8.setOnClickListener { sample8() }
            tvButton9.setOnClickListener { sample9() }
            tvButton10.setOnClickListener { sample10() }
            tvButton11.setOnClickListener { sample11() }
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
     * usage 4 : 构建流的其他基础方法
     * 1. 使用 .asFlow() 扩展函数，可以将各种集合与序列转换为流。
     * 2. flowOf 构建器定义了一个发射固定值集的流。
     */
    private fun sample4() = runBlocking {
        // 将一个整数区间转化为流
        (1..3).asFlow().collect { value -> println(value) }
//        flowOf(1, 2, 3).collect { value -> println(value) }
    }

    /**
     * usage 5 : 过渡操作符。可以使用操作符转换流，就像使用集合与序列一样。
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
        return "response: $request"
    }

    /**
     * usage 6 : 转换操作符。我们可以 发射 任意值任意次。
     * 1. 使用 transform 我们可以在执行长时间运行的异步请求之前发射一个字符串并跟踪这个响应：
     */
    private fun sample6() = runBlocking {
        (1..3).asFlow() // 一个请求流
            .transform { request ->
                emit("Making request $request")
                emit(performRequest(request))
            }
            .collect { response -> println(response) }
    }

    private fun numbersFlow(): Flow<Int> = flow {
        try {
            emit(1)
            emit(2)
            println("This line will not execute")
            emit(3)
        } finally {
            println("Finally in numbers")
        }
    }

    /**
     * usage 7 : 限长操作符。
     * 1. 限长过渡操作符（例如 take）在流触及相应限制的时候会将它的执行取消。
     * 2. 协程中的取消操作总是通过抛出异常来执行，这样所有的资源管理函数（如 try {...} finally {...} 块）会在取消的情况下正常运行
     */
    private fun sample7() = runBlocking {
        numbersFlow()
            .take(2) // 只获取前两个
            .collect { value -> println(value) }
    }

    /**
     * usage 8 : 末端流操作符。
     * 末端操作符是在流上用于启动流收集的挂起函数。 collect 是最基础的末端操作符，但是还有另外一些更方便使用的末端操作符：
     * 1. 转化为各种集合，例如 toList 与 toSet。
     * 2. 获取第一个（first）值与确保流发射单个（single）值的操作符。
     * 3. 使用 reduce 与 fold 将流规约到单个值。
     */
    private fun sample8() = runBlocking {
        val sum1 = (1..5).asFlow()
            .map { it * it } // 数字 1 至 5 的平方
            .reduce { a, b ->
                println("a: $a, b: $b")
                a + b // 自定义操作为累加运算
            } // 求和（末端操作符）
        println(sum1)

        val sum2 = (1..5).asFlow()
            .map { it * it } // 数字 1 至 5 的平方
            .fold(10) { c, d ->// 给定一个初始值
                println("c: $c, d: $d")
                c + d // 自定义操作为累加运算
            }
        println(sum2)

        val firstVal = (1..3).asFlow().first()
        println("firstVal: $firstVal")

        val singleVal = flowOf("this is single flow").single()
        println("singleVal: $singleVal")

        val toList = (1..3).asFlow().toList()
        println("toList: $toList")

        val toSet = (1..3).asFlow().toSet()
        println("toSet: $toSet")
    }

    /**
     * usage 9 : 流是连续的。
     * 1. 流的每次单独收集都是按顺序执行的，除非进行特殊操作的操作符使用多个流。
     * 2. 该收集过程直接在协程中运行，该协程调用末端操作符。
     * 3. 默认情况下不启动新协程。 从上游到下游每个过渡操作符都会处理每个发射出的值然后再交给末端操作符。
     */
    private fun sample9() = runBlocking {
        (1..5).asFlow()
            .filter {
                println("Filter $it")
                it % 2 == 0
            }
            .map {
                println("Map $it")
                "string $it"
            }.collect {
                println("Collect $it")
            }
    }

    private fun simpleFlow(): Flow<Int> = flow {
        log("Started simple flow")
        for (i in 1..3) {
            emit(i)
        }
    }

    /**
     * usage 10 : 流的上下文
     * 1. 流的收集总是在调用协程的上下文中发生。
     * 2. 它不关心执行的上下文并且不会阻塞调用者
     * 例如，如果有一个流 simple，然后以下代码在它的编写者指定的上下文中运行，而无论流 simple 的实现细节如何：
     */
    private fun sample10() = runBlocking {
        simpleFlow().collect { value ->
            log("Collected1: $value") // 运行在指定上下文中: [main]
        }
        withContext(Dispatchers.Default) {
            simpleFlow().collect { value ->
                log("Collected2: $value") // 运行在指定上下文中: [DefaultDispatcher-worker-1]
            }
        }

        // 使用 withContext 发生错误的情况
        // flow {...} 构建器中的代码必须遵循上下文保存属性，并且不允许从其他上下文中发射（emit）。

        /*
         * java.lang.IllegalStateException: Flow invariant is violated:
         * Flow was collected in [BlockingCoroutine{Active}@98507f9, BlockingEventLoop@9a8733e],
         * but emission happened in [DispatchedCoroutine{Active}@fca259f, Dispatchers.Default].
         * Please refer to 'flow' documentation or use 'flowOn' instead
         */

        /*
        val contextFlow = flow {
            // 在流构建器中更改消耗 CPU 代码的上下文的错误方式
            withContext(Dispatchers.Default) {
                for (i in 1..3) {
                    delay(100) // 假装我们以消耗 CPU 的方式进行计算
                    emit(i) // 发射下一个值
                }
            }
        }
        runBlocking {
            // 在主线程Main收集，却在 Dispatchers.Default 发射值，会出现 IllegalStateException
            contextFlow.collect { value -> println(value) }
        }
        */
    }

    /**
     * usage 11 : flowOn 操作符
     * 该函数用于更改流发射的上下文。
     * 以下示例展示了更改流上下文的正确方法，该示例还通过打印相应线程的名字以展示它们的工作方式：
     */
    private fun sample11() {
        // 在 [DefaultDispatcher-worker-1] 发射值
        val flow = flow {
            for (i in 1..3) {
                delay(100) // 假装我们以消耗 CPU 的方式进行计算
                log("Emitting $i")
                emit(i) // 发射下一个值
            }
        }.flowOn(Dispatchers.Default)

        runBlocking {
            // 在 [main] 中收集值
            flow.collect { value ->
                log("Collected $value")
            }
        }
        // 发射值 和 收集值 处于不同的协程和线程中，并发运行
    }

    private fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

}