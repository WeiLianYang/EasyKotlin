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

package com.william.easykt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.william.base_component.utils.toStringArray
import com.william.easykt.R
import com.william.easykt.data.UsageBean
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf


/**
 * @author William
 * @date 2021/8/27 22:19
 * Class Comment：
 */
class SampleViewModel : ViewModel() {

    private val flowSampleDataFlow = flowOf(
        listOf(
            UsageBean(R.string.test_kotlin_flow1),
            UsageBean(R.string.test_kotlin_flow2),
            UsageBean(R.string.test_kotlin_flow3),
            UsageBean(R.string.test_kotlin_flow4),
            UsageBean(R.string.test_kotlin_flow5),
            UsageBean(R.string.test_kotlin_flow6),
            UsageBean(R.string.test_kotlin_flow7),
            UsageBean(R.string.test_kotlin_flow8),
            UsageBean(R.string.test_kotlin_flow9),
            UsageBean(R.string.test_kotlin_flow10),
            UsageBean(R.string.test_kotlin_flow11),
            UsageBean(R.string.test_kotlin_flow12),
            UsageBean(R.string.test_kotlin_flow13),
            UsageBean(R.string.test_kotlin_flow14),
            UsageBean(R.string.test_kotlin_flow15),
            UsageBean(R.string.test_kotlin_flow16),
            UsageBean(R.string.test_kotlin_flow17),
            UsageBean(R.string.test_kotlin_flow18),
            UsageBean(R.string.test_kotlin_flow19),
            UsageBean(R.string.test_kotlin_flow20),
            UsageBean(R.string.test_kotlin_flow21),
        )
    )

    val flowSampleDataList = flowSampleDataFlow.asLiveData()

    private val channelSampleDataFlow = flowOf(
        listOf(
            UsageBean(R.string.test_kotlin_channel1),
            UsageBean(R.string.test_kotlin_channel2),
            UsageBean(R.string.test_kotlin_channel3),
            UsageBean(R.string.test_kotlin_channel4),
            UsageBean(R.string.test_kotlin_channel5),
            UsageBean(R.string.test_kotlin_channel6),
            UsageBean(R.string.test_kotlin_channel7),
            UsageBean(R.string.test_kotlin_channel8),
            UsageBean(R.string.test_kotlin_channel9),
            UsageBean(R.string.test_kotlin_channel10),
            UsageBean(R.string.test_kotlin_channel11)
        )
    )

    val channelSampleDataList = channelSampleDataFlow.asLiveData()

    val coroutinesSampleData = flow {
        val array = R.array.coroutines_string_array.toStringArray()
        val list = array.map { UsageBean(title = it) }
        emit(list)
    }.asLiveData()

}