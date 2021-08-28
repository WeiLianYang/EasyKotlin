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
import com.william.base_component.utils.logW
import com.william.base_component.utils.toPx
import com.william.easykt.R
import com.william.easykt.databinding.ActivityFlowSampleBinding
import com.william.easykt.ui.adapter.FlowAdapter
import com.william.easykt.viewmodel.FlowSampleViewModel
import com.zyyoona7.itemdecoration.RecyclerViewDivider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

/**
 * author : WilliamYang
 * date : 2021/7/30 10:41
 * description : Kotlin flow demo
 * @see <a href="http://www.kotlincn.net/docs/reference/coroutines/flow.html">异步流</a>
 */
class FlowSampleActivity : BaseActivity() {

    override val viewBinding: ActivityFlowSampleBinding by bindingView()
    private lateinit var mAdapter: FlowAdapter
    private val viewModel by viewModels<FlowSampleViewModel>()

    override fun initData() {
        setTitleText("FlowSample")
    }

    override fun initAction() {
        viewBinding.recyclerView.apply {
            adapter = FlowAdapter().also {
                mAdapter = it
            }
            RecyclerViewDivider.staggeredGrid()
                .includeStartEdge()
                .includeEdge().spacingSize(R.dimen.dp_10.toPx())
                .build().addTo(this)
        }

        viewModel.dataList.observe(this, {
            mAdapter.setList(it)
        })
        mAdapter.setOnItemClickListener { _, position, _ ->
            when (position) {
                0 -> sample1()
                1 -> sample2()
                2 -> sample3()
                3 -> sample4()
                4 -> sample5()
                5 -> sample6()
                6 -> sample7()
                7 -> sample8()
                8 -> sample9()
                9 -> sample10()
                10 -> sample11()
                11 -> sample12()
                12 -> sample13()
                13 -> sample14()
                14 -> {
                    sample15Zip()
                    sample15Combine()
                }
                15 -> {
                    sample16()
                    sample16FlatMapConcat()
                    sample16FlatMapMerge()
                    sample16FlatMapLatest()
                }
                16 -> sample17()
                17 -> sample18()
                18 -> {
                    sample19()
                    sample19Collect()
                }
                19 -> sample20()
                else -> {
                }
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
        println("----> sample 1")
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
        println("----> sample 2")
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
        println("----> sample 3")
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
        println("----> sample 4")
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
        println("----> sample 5")
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
        println("----> sample 6")
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
        println("----> sample 7")
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
        println("----> sample 8")
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
        println("----> sample 9")
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
        println("----> sample 10")
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
     * 1. 该函数用于更改流发射的上下文。
     * 以下示例展示了更改流上下文的正确方法，该示例还通过打印相应线程的名字以展示它们的工作方式：
     */
    private fun sample11() {
        println("----> sample 11")
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

    private fun intFlow(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100) // 假装我们异步等待了 100 毫秒
            emit(i) // 发射下一个值
        }
    }

    /**
     * usage 12 : 对流缓冲
     * 注意，当必须更改 CoroutineDispatcher 时，flowOn 操作符使用了相同的缓冲机制
     * 我们可以在流上使用 buffer 操作符来并发运行这个 flow 流中发射元素的代码以及收集的代码，而不是顺序运行它们：
     */
    private fun sample12() {
        println("----> sample 12")
        runBlocking {
            val time = measureTimeMillis {
                intFlow()
                    .buffer() // 缓冲发射项，无需等待。并发的发射值
                    .collect { value ->
                        delay(300) // 假装我们花费 300 毫秒来处理它
                        println(value)
                    }
            }
            println("Collected in $time ms")// 耗时少 2个 100ms
        }
    }

    /**
     * usage 13 : 合并
     * 1. 当流代表部分操作结果或操作状态更新时，可能没有必要处理每个值，而是只处理最新的那个。
     * 在本示例中，当收集器处理它们太慢的时候，conflate 操作符可以用于跳过中间值。
     */
    private fun sample13() {
        println("----> sample 13")
        runBlocking {
            val time = measureTimeMillis {
                intFlow()
                    .conflate() // 合并发射项，不对每个值进行处理
                    .collect { value ->
                        delay(300) // 假装我们花费 300 毫秒来处理它
                        println(value)// 1,3 虽然第一个数字仍在处理中，但第二个和第三个数字已经产生，因此第二个是 conflated ，只有最新的（第三个）被交付给收集器：
                    }
            }
            println("Collected in $time ms")
        }
    }

    /**
     * usage 14 : 处理最新值
     * 1. 当发射器和收集器都很慢的时候，合并是加快处理速度的一种方式。它通过删除发射值来实现。
     * 另一种方式是取消缓慢的收集器，并在每次发射新值的时候重新启动它。
     * 有一组与 xxx 操作符执行相同基本逻辑的 xxxLatest 操作符，但是在新值产生的时候取消执行其块中的代码。
     * 输出结果：
     * Collecting 1
     * Collecting 2
     * Collecting 3
     * Done 3
     * Collected in 703 ms
     */
    private fun sample14() {
        println("----> sample 14")
        runBlocking {
            val time = measureTimeMillis {
                intFlow()
                    .collectLatest { value -> // 取消并重新发射最后一个值
                        println("Collecting $value")
                        delay(300) // 假装我们花费 300 毫秒来处理它
                        println("Done $value")
                    }
            }
            println("Collected in $time ms")
        }
    }

    /**
     * usage 15 : 组合多个流
     * 1. flowA.zip(flowB)
     *
     * 输出结果：
     * zip : 1 -> one at 467 ms from start
     * zip : 2 -> two at 869 ms from start
     * zip : 3 -> three at 1276 ms from start
     */
    private fun sample15Zip() {
        /*runBlocking {
            val flowA = (1..3).asFlow() // 数字 1..3
            val flowB = flowOf("one", "two", "three") // 字符串
            flowA.zip(flowB) { a, b ->
                "$a -> $b"  // 组合单个字符串
            }.collect {
                println(it) // 收集并打印
            }
        }*/
        println("----> sample 15Zip")
        runBlocking {
            val flowA = (1..3).asFlow().onEach { delay(300) } // 发射数字 1..3，间隔 300 毫秒
            val flowB = flowOf("one", "two", "three").onEach { delay(400) } // 每 400 毫秒发射一次字符串
            val startTime = System.currentTimeMillis() // 记录开始的时间
            flowA.zip(flowB) { a, b -> "$a -> $b" } // 使用“zip”组合单个字符串
                .collect { value -> // 收集并打印
                    println("zip : $value at ${System.currentTimeMillis() - startTime} ms from start")
                }
        }
    }

    /**
     * usage 15 : 组合多个流
     * 1. flowA.combine(flowB)
     * 当组合中的流每次发射新值后，都会执行收集
     *
     * 输出结果：
     * combine : 1 -> one at 410 ms from start
     * combine : 2 -> one at 613 ms from start
     * combine : 2 -> two at 814 ms from start
     * combine : 3 -> two at 916 ms from start
     * combine : 3 -> three at 1218 ms from start
     */
    private fun sample15Combine() {
        println("----> sample 15 Combine")
        runBlocking {
            val flowA = (1..3).asFlow().onEach { delay(300) } // 发射数字 1..3，间隔 300 毫秒
            val flowB = flowOf("one", "two", "three").onEach { delay(400) } // 每 400 毫秒发射一次字符串
            val startTime = System.currentTimeMillis() // 记录开始的时间
            flowA.combine(flowB) { a, b -> "$a -> $b" } // 使用“zip”组合单个字符串
                .collect { value -> // 收集并打印
                    println("combine : $value at ${System.currentTimeMillis() - startTime} ms from start")
                }
        }
    }

    private fun requestFlow(i: Int): Flow<String> = flow {
        emit("$i: First")
        delay(500) // 等待 500 毫秒
        emit("$i: Second")
    }

    /**
     * usage 16 : 展平流
     * 1. 将流中的值转换为单个流进行下一步处理
     *
     * 输出结果：
     * 1: First
     * 1: Second
     * 2: First
     * 2: Second
     * 3: First
     * 3: Second
     */
    private fun sample16() {
        println("----> sample 16")
        // 得到了一个包含流的流（Flow<Flow<String>>），需要将其进行展平为单个流以进行下一步处理
        val newFlow: Flow<Flow<String>> = (1..3).asFlow().map { requestFlow(it) }
        runBlocking {
            newFlow.collect { flow ->
                flow.collect { println(it) }
            }
        }
    }

    /**
     * usage 16 : 展平流 flatMapConcat
     * 顺序收集
     *
     * 输出结果：
     * flatMapConcat : 1: First at 112 ms from start
     * flatMapConcat : 1: Second at 613 ms from start
     * flatMapConcat : 2: First at 715 ms from start
     * flatMapConcat : 2: Second at 1218 ms from start
     * flatMapConcat : 3: First at 1321 ms from start
     * flatMapConcat : 3: Second at 1822 ms from start
     */
    private fun sample16FlatMapConcat() {
        println("----> sample 16 flatMapConcat")
        runBlocking {
            val startTime = System.currentTimeMillis() // 记录开始时间
            (1..3).asFlow()
                .onEach { delay(100) } // 每 100 毫秒发射一个数字
                .flatMapConcat { requestFlow(it) }
                .collect { value -> // 收集并打印
                    println("flatMapConcat : $value at ${System.currentTimeMillis() - startTime} ms from start")
                }
        }
    }

    /**
     * usage 16 : 展平流 flatMapMerge
     *
     * 并发收集所有传入的流，并将它们的值合并到一个单独的流，以便尽快的发射值
     *
     * 输出结果：
     * flatMapMerge : 1: First at 143 ms from start
     * flatMapMerge : 2: First at 241 ms from start
     * flatMapMerge : 3: First at 346 ms from start
     * flatMapMerge : 1: Second at 645 ms from start
     * flatMapMerge : 2: Second at 744 ms from start
     * flatMapMerge : 3: Second at 851 ms from start
     *
     * 注意，flatMapMerge 会顺序调用代码块（本示例中的 { requestFlow(it) }），
     * 但是并发收集结果流，相当于执行顺序是首先执行 map { requestFlow(it) } 然后在其返回结果上调用 flattenMerge。
     */
    private fun sample16FlatMapMerge() {
        println("----> sample 16 sample16FlatMapMerge")
        runBlocking {
            val startTime = System.currentTimeMillis() // 记录开始时间
            (1..3).asFlow()
                .onEach { delay(100) } // 每 100 毫秒发射一个数字
                .flatMapMerge { requestFlow(it) }
                .collect { value -> // 收集并打印
                    println("flatMapMerge : $value at ${System.currentTimeMillis() - startTime} ms from start")
                }
        }
    }

    /**
     * usage 16 : 展平流 flatMapLatest
     *
     * 收集最新的流，在发出新流后立即取消先前流的收集
     *
     * 输出结果：
     * flatMapLatest : 1: First at 111 ms from start
     * flatMapLatest : 2: First at 217 ms from start
     * flatMapLatest : 3: First at 321 ms from start
     * flatMapLatest : 3: Second at 824 ms from start
     *
     * 注意，flatMapLatest 在一个新值到来时取消了块中的所有代码 (本示例中的 { requestFlow(it) }）。
     * 这在该特定示例中不会有什么区别，由于调用 requestFlow 自身的速度是很快的，不会发生挂起，所以不会被取消。
     * 然而，如果我们要在块中调用诸如 delay 之类的挂起函数，这将会被表现出来。
     */
    private fun sample16FlatMapLatest() {
        println("----> sample 16 flatMapLatest")
        runBlocking {
            val startTime = System.currentTimeMillis() // 记录开始时间
            (1..3).asFlow()
                .onEach { delay(100) } // 每 100 毫秒发射一个数字
                .flatMapLatest { requestFlow(it) }
                .collect { value -> // 收集并打印
                    println("flatMapLatest : $value at ${System.currentTimeMillis() - startTime} ms from start")
                }
        }
    }

    /**
     * usage 17 : 收集者可以使用 Kotlin 的 try/catch 块来处理异常：
     * 实际上捕获了在发射器或任何过渡或末端操作符中发生的任何异常
     *
     * 输出结果：
     * string 1
     * Caught java.lang.IllegalStateException: Crashed on 2
     */
    private fun sample17() {
        println("----> sample 17")
        runBlocking {
            try {
                (1..3).asFlow().map { value ->
                    check(value <= 1) { "Crashed on $value" }
                    "string $value"
                }.collect { value ->
                    println(value)
                }
            } catch (e: Throwable) {
                println("Caught $e")
            }
        }
    }

    /**
     * usage 18 : 异常透明性
     * 1. 流必须对异常透明，即在 flow { ... } 构建器内部的 try/catch 块中发射值是违反异常透明性的。
     *    这样可以保证收集器抛出的一个异常能被像先前示例中那样的 try/catch 块捕获。
     * 2. 发射器可以使用 catch 操作符来保留此异常的透明性并允许封装它的异常处理。
     *    catch 操作符的代码块可以分析异常并根据捕获到的异常以不同的方式对其做出反应：
     *      - 可以使用 throw 重新抛出异常。
     *      - 可以使用 catch 代码块中的 emit 将异常转换为值发射出去。
     *      - 可以将异常忽略，或用日志打印，或使用一些其他代码处理它。
     *
     * 输出结果：
     * string 1
     * Caught java.lang.IllegalStateException: Crashed on 2
     * 1
     * Caught java.lang.IllegalStateException: Collected 2
     */
    private fun sample18() {
        println("----> sample 18")
        runBlocking {
            flowOf(1, 2, 3)
                .map { value ->
                    check(value <= 1) { "Crashed on $value" }
                    "string $value"
                }
                .catch { e -> emit("Caught $e") } // 发射一个异常
                .collect { value -> println(value) }

            // catch 过渡操作符遵循异常透明性，仅捕获上游异常（catch 操作符上游的异常，但是它下面的不是）。
            // 如果 collect { ... } 块（位于 catch 之下）抛出一个异常，那么异常会逃逸：
            /*
            flowOf(1, 2, 3)
                 .catch { e -> println("Caught $e") } // 不会捕获下游异常，会发生崩溃
                 .collect { value ->
                     check(value <= 1) { "Collected $value" }
                     println(value)
                 }
                 */

            // 声明式捕获
            flowOf(1, 2, 3)
                .onEach { value ->
                    check(value <= 1) { "Collected $value" }
                    println(value)
                }
                // 将 check 放到 catch 操作符之前 就能捕获所有异常
                .onCompletion { cause -> "flow complete with $cause".logW() }
                .catch { e ->
                    println("Caught $e")
//                    throw e // if throw, app will crash
                }
                .collect()
        }
    }


    /**
     * usage 19 : 流完成
     * 当流收集完成时（普通情况或异常情况），它可能需要执行一个动作。它可以通过两种方式完成：命令式 或 声明式。
     *
     * 1. 命令式 finally 块
     * 2. 声明式 处理：流拥有 onCompletion 过渡操作符，它在流完全收集时调用
     *
     * 输出结果：
     * 1
     * 2
     * 3
     * Done
     * string 1
     * Flow completed exception
     * Caught exception
     *
     * string 1
     * Flow completed with java.lang.IllegalStateException: Crashed on 2
     * Caught exception
     */
    private fun sample19() {
        println("----> sample 19")
        runBlocking {
            try {
                (1..3).asFlow().collect { value -> println(value) }
            } finally {
                println("Done")
            }
            (1..3).asFlow()
                .onCompletion { println("Done") }
                .collect { value -> println(value) }

            // onCompletion 的主要优点是其 lambda 表达式的可空参数 Throwable 可以用于确定流收集是正常完成还是有异常发生。
            // 在下面的示例中 simple 流在发射数字 1 之后抛出了一个异常：
            // onCompletion 操作符与 catch 不同，它不处理异常。我们可以看到前面的示例代码，异常仍然流向下游。
            // 它将被提供给后面的 onCompletion 操作符，并可以由 catch 操作符处理。
            (1..3).asFlow()
                .map { value ->
                    check(value <= 1) { "Crashed on $value" }
                    "string $value"
                }
                .onCompletion { cause -> if (cause != null) println("Flow completed exceptionally") } // 2 观察上游异常
                .catch { e -> println("Caught exception: $e") } // 3 需要放在 onCompletion 之后，否则 onCompletion 不执行
                .collect { value -> println(value) } // 1

            // 与 catch 操作符的另一个不同点是
            // onCompletion 能观察到所有异常并且仅在上游流成功完成（没有取消或失败）的情况下接收一个 null 异常。
            (1..3).asFlow()
                .onCompletion { cause -> println("Flow completed with $cause") } // 2 观察到下游异常
                .map { value ->
                    check(value <= 1) { "Crashed on $value" }
                    "string $value"
                }
                .catch { e -> println("Caught exception: $e") } // 3 需要放在 onCompletion 之后，否则 onCompletion 不执行
                .collect { value ->
                    println(value) // 1
                }
        }
    }

    /**
     * usage 19 : 启动流收集
     * launchIn 指定一个协程来完成流的收集，不阻塞当前协程
     *
     * 输出结果：
     * Event: 1
     * Event: 2
     * Event: 3
     * Done
     *
     * Done
     * Event: 1
     * Event: 2
     * Event: 3
     */
    private fun sample19Collect() {
        println("----> sample 19 collect")
        runBlocking {
            // 模仿事件流
            val events = (1..3).asFlow().onEach { delay(100) }

            events.onEach { event -> println("Event: $event") }
                .collect() // <--- 等待流收集
            println("Done")
        }

        runBlocking {
            // 模仿事件流
            val events = (1..3).asFlow().onEach { delay(100) }

            events.onEach { event -> println("Event: $event") }
                .launchIn(this) // <--- 在单独的协程中执行流
            println("Done")
        }
    }

    /**
     * usage 20 : 流取消检测
     * 为方便起见，流构建器对每个发射值执行附加的 ensureActive 检测以进行取消。
     * 这意味着从 flow { ... } 发出的繁忙循环是可以取消的：
     *
     * 输出结果：
     * 1
     * 2
     * 3
     * Exception in thread "main" kotlinx.coroutines.JobCancellationException:
     * BlockingCoroutine was cancelled; job="coroutine#1":BlockingCoroutine{Cancelled}@5ec0a365
     */
    private fun sample20() {
        println("----> sample 20")
        runBlocking {
            (1..5).asFlow().cancellable().collect { value ->
                if (value == 3) cancel()
                println("value: $value")
            }
        }
    }

    private fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

}
