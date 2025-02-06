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
import com.william.base_component.extension.*
import com.william.easykt.R
import com.william.easykt.databinding.ActivityChannelSampleBinding
import com.william.easykt.ui.adapter.UsageAdapter
import com.william.easykt.viewmodel.SampleViewModel
import com.zyyoona7.itemdecoration.RecyclerViewDivider
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.selects.select
import kotlin.random.Random

/**
 * author : WilliamYang
 * date : 2021/8/29 16:36
 * description : Kotlin channel demo
 * @see <a href="http://www.kotlincn.net/docs/reference/coroutines/channels.html">通道 / 热数据流，用于协程之间通信</a>
 */
class ChannelSampleActivity : BaseActivity() {

    override val viewBinding: ActivityChannelSampleBinding by bindingView()
    private lateinit var mAdapter: UsageAdapter
    private val viewModel by viewModels<SampleViewModel>()

    override fun initData() {
        setTitleText("Channel Sample")
    }

    override fun initAction() {
        viewBinding.recyclerView.apply {
            adapter = UsageAdapter().also {
                mAdapter = it
            }
            RecyclerViewDivider.linear()
                .asSpace().dividerSize(R.dimen.dp_10.toPx())
                .build().addTo(this)
        }

        viewModel.channelSampleDataList.observe(this) {
            mAdapter.setList(it)
        }
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
                else -> {
                }
            }
        }
    }

    /**
     * usage 1 : 通道基础
     * 1. 延期的值提供了一种便捷的方法使单个值在多个协程之间进行相互传输。通道提供了一种在流中传输值的方法。
     * 2. 一个 Channel 是一个和 BlockingQueue 非常相似的概念。
     *    其中一个不同是它代替了阻塞的 put 操作并提供了挂起的 send，
     *    还替代了阻塞的 take 操作并提供了挂起的 receive。
     *
     * 输出结果：
     *    -> 1
     *    -> 4
     *    -> 9
     *    -> 16
     *    -> 25
     *    -> Done!
     */
    private fun sample1() = runBlocking {
        "sample 1".logV()
        val channel = Channel<Int>()
        launch {
            // 这里可能是消耗大量 CPU 运算的异步逻辑，我们将仅仅做 5 次整数的平方并发送
            for (x in 1..5) channel.send(x * x)
        }
        // 这里我们打印了 5 次被接收的整数：
        repeat(5) { "${channel.receive()}".logD() }
        "Done!".logI()
    }

    /**
     * usage 2 : 关闭与迭代通道
     * 1. 和队列不同，一个通道可以通过被关闭来表明没有更多的元素将会进入通道。 在
     *    接收者中可以定期的使用 for 循环来从通道中接收元素。
     * 2. 从概念上来说，一个 close 操作就像向通道发送了一个特殊的关闭指令。
     *    这个迭代停止就说明关闭指令已经被接收了。所以这里保证所有先前发送出去的元素都在通道关闭前被接收到。
     *
     * 输出结果：同 sample1()
     */
    private fun sample2() = runBlocking {
        "sample 2".logV()
        val channel = Channel<Int>()
        launch {
            for (x in 1..5) channel.send(x * x)
            channel.close() // 我们结束发送
        }
        // 这里我们使用 `for` 循环来打印所有被接收到的元素（直到通道被关闭）
        for (y in channel) "$y".logD()
        "Done!".logI()
    }

    /**
     * usage 3 : 构建通道生产者
     *
     * 输出结果：同 sample1()
     */
    private fun sample3() = runBlocking {
        "sample 3".logV()
        val squares = produceSquares()
        squares.consumeEach { "$it".logD() }
        "Done!".logI()
    }

    /**
     * usage 4 : 管道
     * 1. 管道是一种一个协程在流中开始生产可能无穷多个元素的模式：
     *    并且另一个或多个协程开始消费这些流，做一些操作，并生产了一些额外的结果。
     */
    private fun sample4() = runBlocking {
        "sample 4".logV()
        val numbers = produceNumbers() // produces integers from 1 and on
        val squares = square(numbers) // squares integers
        repeat(5) {
            "${squares.receive()}".logD() // print first five
        }
        "Done!".logI() // we are done
        coroutineContext.cancelChildren() // cancel children coroutines
    }

    /**
     * usage 5 : 使用管道的素数  在协程中使用一个管道来生成素数
     *
     * 输出结果：
     * -> 2
     * -> 3
     * -> 5
     * -> 7
     * -> 11
     * -> 13
     * -> 17
     * -> 19
     * -> 23
     * -> 29
     */
    private fun sample5() = runBlocking {
        "sample 5".logV()
        var cur = numbersFrom(2)
        repeat(10) {
            val prime = cur.receive()
            "prime: $prime".logD()
            cur = filter(cur, prime)
        }
        coroutineContext.cancelChildren() // cancel all children to let main finish
    }

    /**
     * usage 6 : 扇出
     * 1. 注意，取消生产者协程将关闭它的通道，从而最终终止处理器协程正在执行的此通道上的迭代。
     */
    private fun sample6() = runBlocking {
        "sample 6".logV()
        val producer = produceNumbersDelay()
        repeat(5) {
            launchProcessor(it, producer)
        }
        delay(950)
        producer.cancel() // cancel producer coroutine and thus kill them all
    }

    /**
     * usage 7 : 扇入
     * 1. 多个协程可以发送到同一个通道。
     *    比如说，让我们创建一个字符串的通道，和一个在这个通道中以指定的延迟反复发送一个指定字符串的挂起函数：
     */
    private fun sample7() = runBlocking {
        "sample 7".logV()
        val channel = Channel<String>()
        launch { sendString(channel, "foo", 200L) }
        launch { sendString(channel, "BAR!", 500L) }
        repeat(6) { // receive first six
            channel.receive().logD()
        }
        coroutineContext.cancelChildren() // cancel all children to let main finish
    }

    /**
     * usage 8 : 带缓冲的通道
     * 1. 无缓冲的通道在发送者和接收者相遇时传输元素（也称“对接”）。
     * 2. 如果发送先被调用，则它将被挂起直到接收被调用，如果接收先被调用，它将被挂起直到发送被调用。
     *
     * 输出结果：
     * -> Sending 0
     * -> Sending 1
     * -> Sending 2
     * -> Sending 3
     * -> Sending 4
     * -> cancel sender coroutine
     */
    private fun sample8() = runBlocking {
        "sample 8".logV()
        val channel = Channel<Int>(4) // create buffered channel
        val sender = launch { // launch sender coroutine
            repeat(10) {
                "Sending $it".logD() // print before sending each element
                // 前四个元素被加入到了缓冲区并且发送者在试图发送第五个元素的时候被挂起
                channel.send(it) // will suspend when buffer is full
            }
        }
        // don't receive anything... just wait....
        delay(1000)
        "cancel sender coroutine".logV()
        sender.cancel() // cancel sender coroutine
    }

    /**
     * usage 9 : 通道是公平的
     * 发送和接收操作是 公平的 并且尊重调用它们的多个协程。
     * 它们遵守先进先出原则，可以看到第一个协程调用 receive 并得到了元素。
     * 在下面的例子中两个协程“乒”和“乓”都从共享的“桌子”通道接收到这个“球”元素。
     */
    private fun sample9() = runBlocking {
        val table = Channel<Ball>() // 一个共享的 table（桌子）
        launch {
            player("ping", table)
        }
        launch {
            player("pong", table)
        }
        table.send(Ball(0)) // 乒乓球
        delay(1000) // 延迟 1 秒钟
        coroutineContext.cancelChildren() // 游戏结束，取消它们
    }

    /**
     * usage 10 : 计时器通道
     * 计时器通道是一种特别的会合通道，每次经过特定的延迟都会从该通道进行消费并产生 Unit。
     * 虽然它看起来似乎没用，它被用来构建分段来创建复杂的基于时间的 produce 管道和进行窗口化操作以及其它时间相关的处理。
     * 可以在 select 中使用计时器通道来进行“打勾”操作。
     */
    private fun sample10() = runBlocking {
        val tickerChannel =
            ticker(delayMillis = 100, initialDelayMillis = 0) // create ticker channel
        var nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
        println("Initial element is available immediately: $nextElement") // no initial delay

        nextElement =
            withTimeoutOrNull(50) { tickerChannel.receive() } // all subsequent elements have 100ms delay
        println("Next element is not ready in 50 ms: $nextElement")

        nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
        println("Next element is ready in 100 ms: $nextElement")

        // Emulate large consumption delays
        println("Consumer pauses for 150ms")
        delay(150)
        // Next element is available immediately
        nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
        println("Next element is available immediately after large consumer delay: $nextElement")
        // Note that the pause between `receive` calls is taken into account and next element arrives faster
        nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
        println("Next element is ready in 50ms after consumer pause in 150ms: $nextElement")

        tickerChannel.cancel() // indicate that no more elements are needed
    }

    data class Ball(var hits: Int)

    private suspend fun player(name: String, table: Channel<Ball>) {
        for (ball in table) { // 在循环中接收球
            ball.hits++
            println("$name $ball")
            delay(300) // 等待一段时间
            table.send(ball) // 将球发送回去
        }
    }

    private fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
        for (x in 1..5) send(x * x)
    }

    private fun CoroutineScope.produceNumbers() = produce {
        var x = 1
        // infinite stream of integers starting from 1
        while (true) {
            "produceNumbers: produce".logD()
            send(x++)
        }
    }

    private fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
        for (x in numbers) {
            "square: produce".logD()
            send(x * x)
        }
    }

    private fun CoroutineScope.numbersFrom(start: Int) = produce {
        var x = start
        while (true) send(x++) // infinite stream of integers from start
    }

    private fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce {
        for (x in numbers) {
            "x: $x".logD()
            if (x % prime != 0) send(x)
        }
    }

    private fun CoroutineScope.produceNumbersDelay() = produce {
        var x = 1 // start from 1
        while (true) {
            send(x++) // produce next
            delay(100) // wait 0.1s
        }
    }

    private fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
        for (number in channel) {
            "Processor #$id received $number".logD()
        }
    }

    private suspend fun sendString(channel: SendChannel<String>, s: String, time: Long) {
        while (true) {
            delay(time)
            channel.send(s)
        }
    }

    /**
     * 启动一个生产者协程，其他协程可以使用返回的 receiveChannel 消费数据
     */
    val receiveChannel: ReceiveChannel<Int> = GlobalScope.produce {
        repeat(10) {
            delay(1000)
            send(it)
        }
    }

    /**
     * 启动一个消费者协程，其他协程可以使用返回的 sendChannel 生产数据
     */
    val sendChannel: SendChannel<Int> = GlobalScope.actor {
        val ele = receive()
        while (ele < 10) {
            println("ele: $ele")
        }
    }

    /**
     *  一对多，广播数据流。每个接收端都能收到发送的数据
     *
     * [#0] received: 0
     * [#1] received: 0
     * [#2] received: 0
     * [#0] received: 1
     * [#2] received: 1
     * [#1] received: 1
     * [#0] received: 2
     * [#1] received: 2
     * [#2] received: 2
     */
    private fun sample11() = runBlocking {
        /*
        val broadcastChannel = BroadcastChannel<Int>(Channel.BUFFERED)

        val producer = GlobalScope.launch {
            List(3) { index ->
                delay(1000)
                broadcastChannel.send(index)
            }
            broadcastChannel.close()
        }

        List(3) { index ->
            GlobalScope.launch {
                val receiveChannel = broadcastChannel.openSubscription()
                for (ele in receiveChannel) {
                    println("[#$index] received: $ele")
                }
            }
        }.joinAll()
        */
    }

    /**
     * 复用多个 Channel
     */
    private fun sample12() = runBlocking {
        val channelList = List(10) { Channel<Int>() }

        GlobalScope.launch {
            delay(100)
            val index = Random.nextInt(10)
            channelList[index].send(index * 10)
        }

        val result = select<Int?> {
            channelList.forEach { channel ->
                // 如果 channel 被关闭，select 会直接抛异常
//                channel.onReceive { it }
                // 如果 channel 被关闭，不会抛异常
                channel.onReceiveCatching { it.getOrNull() }
            }
        }
        println(result)
    }

}
