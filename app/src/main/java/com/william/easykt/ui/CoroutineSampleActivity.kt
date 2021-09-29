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
import kotlin.system.measureTimeMillis

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
                4 -> main()
                5 -> sample5()
                6 -> sample6()
                7 -> sample7()
                8 -> sample8()
                9 -> sample9()
                10 -> sample10()
                11 -> sample11()
                12 -> sample12()
                13 -> sample13()
                14 -> sample14()
                15 -> sample15()
                16 -> sample16()
                17 -> sample17()
                18 -> sample18()
                19 -> sample19()
                20 -> sample20()
                21 -> sample21()
                22 -> sample22()
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
     * log :
     * Hello,
     * Coroutines!
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

    /**
     * runBlocking 与 coroutineScope 可能看起来很类似，因为它们都会等待其协程体以及所有子协程结束。
     * 主要区别在于，runBlocking 方法会阻塞当前线程来等待， 而 coroutineScope 只是挂起，会释放底层线程用于其他用途。
     * 由于存在这点差异，runBlocking 是常规函数，而 coroutineScope 是挂起函数。
     *
     * log :
     * Task from coroutine scope
     * Task from runBlocking
     * Task from nested launch
     * Coroutine scope is over
     */
    private fun sample3() = runBlocking { // this: CoroutineScope
        launch {
            delay(200L)
            println("Task from runBlocking")
        }
        coroutineScope { // 创建一个协程作用域
            launch {
                delay(500L)
                println("Task from nested launch")
            }
            delay(100L)
            println("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
        }
        println("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
    }

    /**
     * 一旦 函数调用了 job.cancel，我们在其它的协程中就看不到任何输出，因为它被取消了。
     * 这里也有一个可以使 Job 挂起的函数 cancelAndJoin 它合并了对 cancel 以及 join 的调用。
     *
     * log:
     * job: I'm sleeping 0 ...
     * job: I'm sleeping 1 ...
     * job: I'm sleeping 2 ...
     * main: I'm tired of waiting!
     * main: Now I can quit.
     */
    private fun sample5() = runBlocking {
        val job = launch {
            repeat(1000) { i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(1300L) // 延迟一段时间
        println("main: I'm tired of waiting!")
//        job.cancel() // 取消该作业
//        job.join() // 等待作业执行结束
        job.cancelAndJoin() // 合并了对 cancel 以及 join 的调用
        println("main: Now I can quit.")
    }

    /**
     * 协程的取消是 协作 的。一段协程代码必须协作才能被取消。
     * 所有 kotlinx.coroutines 中的挂起函数都是 可被取消的。它们检查协程的取消，并在取消时抛出 CancellationException。
     * 然而，如果协程正在执行计算任务，并且没有检查取消的话，那么它是不能被取消的，就如如下示例代码所示：
     * 并且我们可以看到它连续打印出了“I'm sleeping”，甚至在调用取消后，作业仍然执行了五次循环迭代并运行到了它结束为止。
     *
     * log :
     * job: I'm sleeping 0 ...
     * job: I'm sleeping 1 ...
     * job: I'm sleeping 2 ...
     * main: I'm tired of waiting!
     * job: I'm sleeping 3 ...
     * job: I'm sleeping 4 ...
     * main: Now I can quit.
     */
    private fun sample6() = runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            while (i < 5) { // 一个执行计算的循环，只是为了占用 CPU
                // 每秒打印消息两次
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("job: I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1300L) // 等待一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消一个作业并且等待它结束
        println("main: Now I can quit.")
    }

    /**
     * 第一种方法是定期调用挂起函数来检查取消。对于这种目的 yield 是一个好的选择。
     * 另一种方法是显式的检查取消状态。让我们试试第二种方法。
     *
     * log :
     * job: I'm sleeping 0 ...
     * job: I'm sleeping 1 ...
     * job: I'm sleeping 2 ...
     * main: I'm tired of waiting!
     */
    private fun sample7() = runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            while (isActive) { // 可以被取消的计算循环
                // 每秒打印消息两次
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("job: I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1300L) // 等待一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并等待它结束
        println("main: Now I can quit.")
    }

    /**
     * log :
     * job: I'm sleeping 0 ...
     * job: I'm sleeping 1 ...
     * job: I'm sleeping 2 ...
     * main: I'm tired of waiting!
     * job: I'm running finally
     * main: Now I can quit.
     */
    private fun sample8() = runBlocking {
        val job = launch {
            try {
                repeat(1000) { i ->
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                println("job: I'm running finally")
            }
        }
        delay(1300L) // 延迟一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并且等待它结束
        println("main: Now I can quit.")
    }

    /**
     * log :
     * job: I'm sleeping 0 ...
     * job: I'm sleeping 1 ...
     * job: I'm sleeping 2 ...
     * main: I'm tired of waiting!
     * job: I'm running finally
     * job: And I've just delayed for 1 sec because I'm non-cancellable
     */
    private fun sample9() = runBlocking {
        val job = launch {
            try {
                repeat(1000) { i ->
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                withContext(NonCancellable) {
                    println("job: I'm running finally")
                    delay(1000L)
                    println("job: And I've just delayed for 1 sec because I'm non-cancellable")
                }
            }
        }
        delay(1300L) // 延迟一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并等待它结束
        println("main: Now I can quit.")
    }

    private suspend fun suspendFun1(): Int {
        delay(1000L)
        return 12
    }

    private suspend fun suspendFun2(): Int {
        delay(1000L)
        return 20
    }

    /**
     * 不是 挂起 函数。它们可以在任何地方使用。
     * 然而，它们总是在调用它们的代码中意味着异步（这里的意思是 并发 ）执行。
     */
    private fun asyncFun1() = GlobalScope.async {
        suspendFun1()
    }

    private fun asyncFun2() = GlobalScope.async {
        suspendFun2()
    }

    /**
     * 顺序调用，会比较耗时
     *
     * log：
     * The answer is 32
     * Completed in 2003 ms
     */
    private fun sample13() = runBlocking {
        val time = measureTimeMillis {
            val one = suspendFun1()
            val two = suspendFun2()
            println("The answer is ${one + two}")
        }
        println("Completed in $time ms")
    }

    /**
     * 并发调用，使用 await 获取结果
     * The answer is 32
     * Completed in 1020 ms
     */
    private fun sample14() = runBlocking {
        val time = measureTimeMillis {
            // 得到 Deferred, 其实是一个job，可以在稍后调用 await() 获取结果
            val one = async { suspendFun1() }
            val two = async { suspendFun2() }
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms")
    }

    /**
     * async 可以通过将 start 参数设置为 CoroutineStart.LAZY 而变为惰性的。
     * 在这个模式下，只有结果通过 await 获取的时候协程才会启动，或者在 Job 的 start 函数调用的时候。
     *
     * The answer is 32
     * Completed in 1020 ms
     */
    private fun sample15() = runBlocking {
        val time = measureTimeMillis {
            val one = async(start = CoroutineStart.LAZY) { suspendFun1() }
            val two = async(start = CoroutineStart.LAZY) { suspendFun2() }
            // 执行一些计算
            one.start() // 启动第一个
            two.start() // 启动第二个
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms")
    }

    private suspend fun concurrentSum(): Int = coroutineScope {
        val one = async { suspendFun1() }
        val two = async { suspendFun2() }
        one.await() + two.await()
    }

    private fun sample16() = runBlocking {
        // 推荐写法。这种情况下，如果在 concurrentSum 函数内部发生了错误，
        // 并且它抛出了一个异常，所有在作用域中启动的协程都会被取消。
        val time = measureTimeMillis {
            println("The answer is ${concurrentSum()}")
        }
        println("Completed in $time ms")

        // 以下写法不推荐
        /*val time = measureTimeMillis {
            // 可以在协程外面启动异步执行
            val one = asyncFun1()
            val two = asyncFun2()
            // 但是等待结果必须调用其它的挂起或者阻塞
            // 当我们等待结果的时候，这里我们使用 `runBlocking { …… }` 来阻塞主线程
            runBlocking {
                println("The answer is ${one.await() + two.await()}")
            }
        }
        println("Completed in $time ms")*/
    }

    /**
     * Second child throws an exception
     * First child was cancelled
     * Computation failed with ArithmeticException
     */
    private fun sample17() = runBlocking {
        try {
            failedConcurrentSum()
        } catch (e: ArithmeticException) {
            println("Computation failed with ArithmeticException")
        }
    }

    /**
     * 请注意，如果其中一个子协程（即 two）失败，第一个 async 以及等待中的父协程都会被取消：
     */
    private suspend fun failedConcurrentSum(): Int = coroutineScope {
        val one = async {
            try {
                delay(5000) // 模拟一个长时间的运算
                42
            } finally {
                println("First child was cancelled")
            }
        }
        val two = async<Int> {
            println("Second child throws an exception")
            // 如果不想让作用域中的协程被取消，捕获异常就可以了
            try {
                throw ArithmeticException()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            50
        }
        one.await() + two.await()
    }

    /**
     * 协程上下文包含一个 协程调度器 （参见 CoroutineDispatcher）它确定了相关的协程在哪个线程或哪些线程上执行。
     * 协程调度器可以将协程限制在一个特定的线程执行，或将它分派到一个线程池，亦或是让它不受限地运行。
     *
     * main runBlocking      : I'm working in thread main
     * Unconfined            : I'm working in thread main
     * Default               : I'm working in thread DefaultDispatcher-worker-1
     * newSingleThreadContext: I'm working in thread MyOwnThread
     */
    private fun sample18() = runBlocking {
        launch { // 运行在父协程的上下文中，即 runBlocking 主协程
            println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
        }
        launch(Dispatchers.Unconfined) { // 不受限的——将工作在主线程中
            println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
        }
        launch(Dispatchers.Default) { // 将会获取默认调度器，和GlobalScope.launch { …… } 使用相同的调度器。
            println("Default               : I'm working in thread ${Thread.currentThread().name}")
        }
//        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        // 一个专用的线程是一种非常昂贵的资源，在真实的应用程序中两者都必须被释放，
        // 当不再需要的时候，使用 close 函数，或存储在一个顶层变量中使它在整个应用程序中被重用。
        launch(newSingleThreadContext("MyOwnThread")) { // 将使它获得一个新的线程
            println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
        }
    }

    /**
     * 非受限调度器 vs 受限调度器
     * 非受限的调度器是一种高级机制，可以在某些极端情况下提供帮助而不需要调度协程以便稍后执行或产生不希望的副作用，
     * 因为某些操作必须立即在协程中执行。非受限调度器不应该在通常的代码中使用。
     *
     * Unconfined      : I'm working in thread main
     * main runBlocking: I'm working in thread main
     * Unconfined      : After delay in thread kotlinx.coroutines.DefaultExecutor
     * main runBlocking: After delay in thread main
     */
    private fun sample19() = runBlocking {
        launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
            println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
            delay(500)
            println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
        }
        launch { // context of the parent, main runBlocking coroutine
            println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
            delay(1000)
            println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
        }
    }

    /**
     * debug log
     */
    private fun sample20() = runBlocking {
        val a = async {
            log("I'm computing a piece of the answer")
            6
        }
        val b = async {
            log("I'm computing another piece of the answer")
            7
        }
        log("The answer is ${a.await() * b.await()}")

        // 协程的 Job 是上下文的一部分，并且可以使用 coroutineContext [Job] 表达式在上下文中检索它：
        // My job is BlockingCoroutine{Active}@c29ebeb
        // 请注意，CoroutineScope 中的 isActive 只是 coroutineContext[Job]?.isActive == true 的一种方便的快捷方式。
        println("My job is ${coroutineContext[Job]}")
    }

    /**
     * 命名协程以用于调试
     */
    private fun sample21() = runBlocking(CoroutineName("main")) {
        log("Started main coroutine")
        // run two background value computations
        val v1 = async(CoroutineName("v1coroutine")) {
            delay(500)
            log("Computing v1")
            252
        }
        val v2 = async(CoroutineName("v2coroutine")) {
            delay(1000)
            log("Computing v2")
            6
        }
        log("The answer for v1 / v2 = ${v1.await() / v2.await()}")
    }

    class ActivityScope {
        private val mainScope = CoroutineScope(Dispatchers.Default) // use Default for test purposes

        fun destroy() {
            mainScope.cancel()
        }

        fun doSomething() {
            // launch ten coroutines for a demo, each working for a different time
            repeat(10) { i ->
                mainScope.launch {
                    delay((i + 1) * 200L) // variable delay 200ms, 400ms, ... etc
                    println("Coroutine $i is done")
                }
            }
        }
    } // class Activity ends

    /**
     * Launched coroutines
     * Coroutine 0 is done
     * Coroutine 1 is done
     * Destroying activity!
     */
    private fun sample22() = runBlocking {
        val activity = ActivityScope()
        activity.doSomething() // run test function
        println("Launched coroutines")
        delay(500L) // delay for half a second
        println("Destroying activity!")
        activity.destroy() // cancels all coroutines
        delay(1000) // visually confirm that they don't work
    }

}

private fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

/**
 * 在 GlobalScope 中启动的活动协程并不会使进程保活。它们就像守护线程。
 *
 * log :
 * I'm sleeping 0 ...
 * I'm sleeping 1 ...
 * I'm sleeping 2 ...
 */
@DelicateCoroutinesApi
private fun main() = runBlocking {
    GlobalScope.launch {
        repeat(10) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L) // 在延迟后退出
}

/**
 * 在具有指定超时的协程中运行给定的挂起代码块，如果超过超时，则抛出TimeoutCancellationException 。
 * 在块内执行的代码在超时时被取消，并且块内可取消挂起函数的活动或下一次调用将引发TimeoutCancellationException 。
 *
 * I'm sleeping 0 ...
 * I'm sleeping 1 ...
 * I'm sleeping 2 ...
 * Exception in thread "main" kotlinx.coroutines.TimeoutCancellationException: Timed out waiting for 1300 ms
 */
fun sample10() = runBlocking {
    withTimeout(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
}

/**
 * 在具有指定超时的协程中运行给定的挂起代码块，如果超过此超时，则返回null 。
 * 在块内执行的代码在超时时被取消，并且块内可取消挂起函数的活动或下一次调用将引发TimeoutCancellationException 。
 * 在超时时抛出异常的兄弟函数是withTimeout 。 请注意，可以使用onTimeout子句为select调用指定超时操作。
 * timeout 事件对于块中运行的代码是异步的，并且可能随时发生，甚至在从 timeout块内部返回之前。
 * 如果您在块内打开或获取某些需要在块外关闭或释放的资源，请记住这一点。 有关详细信息，请参阅协程指南的异步超时和资源部分
 *
 * log :
 * I'm sleeping 0 ...
 * I'm sleeping 1 ...
 * I'm sleeping 2 ...
 * Result is null
 */
private fun sample11() = runBlocking {
    val result = withTimeoutOrNull(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
        "Done" // 在它运行得到结果之前取消它
    }
    println("Result is $result")
}


var acquired = 0

class Resource {
    init {
        acquired++
    } // Acquire the resource

    fun close() {
        acquired--
    } // Release the resource
}

/**
 * This example always prints zero. Resources do not leak.
 */
fun sample12() {
    runBlocking {
        repeat(100_000) { // Launch 100K coroutines
            launch {
                var resource: Resource? = null // Not acquired yet
                try {
                    withTimeout(60) { // Timeout of 60 ms
                        delay(50) // Delay for 50 ms
                        resource = Resource() // Store a resource to the variable if acquired
                    }
                    // We can do something else with the resource here
                } finally {
                    resource?.close() // Release the resource if it was acquired
                }
            }
        }
    }
    // Outside of runBlocking all coroutines have completed
    println(acquired) // Print the number of resources still acquired
}