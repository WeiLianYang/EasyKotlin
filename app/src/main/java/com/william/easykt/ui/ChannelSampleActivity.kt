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
import com.william.base_component.utils.logD
import com.william.base_component.utils.logI
import com.william.base_component.utils.logV
import com.william.base_component.utils.toPx
import com.william.easykt.R
import com.william.easykt.databinding.ActivityChannelSampleBinding
import com.william.easykt.ui.adapter.UsageAdapter
import com.william.easykt.viewmodel.SampleViewModel
import com.zyyoona7.itemdecoration.RecyclerViewDivider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * author : WilliamYang
 * date : 2021/8/29 16:36
 * description : Kotlin channel demo
 * @see <a href="http://www.kotlincn.net/docs/reference/coroutines/channels.html">通道</a>
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

        viewModel.channelSampleDataList.observe(this, {
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
     */
    private fun sample6() = runBlocking {

    }

    /**
     * usage 7 : 扇入
     */
    private fun sample7() = runBlocking {

    }

    /**
     * usage 8 : 带缓冲的通道
     */
    private fun sample8() = runBlocking {

    }

    /**
     * usage 9 : 通道是公平的
     */
    private fun sample9() = runBlocking {

    }

    /**
     * usage 10 : 计时器通道
     */
    private fun sample10() = runBlocking {

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

}
