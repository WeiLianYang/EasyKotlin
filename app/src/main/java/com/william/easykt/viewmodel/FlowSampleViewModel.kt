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
import com.william.easykt.R
import com.william.easykt.data.FlowUsageBean
import kotlinx.coroutines.flow.flowOf


/**
 * @author William
 * @date 2021/8/27 22:19
 * Class Comment：
 */
class FlowSampleViewModel : ViewModel() {

    private val dataFlow = flowOf(
        listOf(
            FlowUsageBean(R.string.test_kotlin_flow1),
            FlowUsageBean(R.string.test_kotlin_flow2),
            FlowUsageBean(R.string.test_kotlin_flow3),
            FlowUsageBean(R.string.test_kotlin_flow4),
            FlowUsageBean(R.string.test_kotlin_flow5),
            FlowUsageBean(R.string.test_kotlin_flow6),
            FlowUsageBean(R.string.test_kotlin_flow7),
            FlowUsageBean(R.string.test_kotlin_flow8),
            FlowUsageBean(R.string.test_kotlin_flow9),
            FlowUsageBean(R.string.test_kotlin_flow10),
            FlowUsageBean(R.string.test_kotlin_flow11),
            FlowUsageBean(R.string.test_kotlin_flow12),
            FlowUsageBean(R.string.test_kotlin_flow13),
            FlowUsageBean(R.string.test_kotlin_flow14),
            FlowUsageBean(R.string.test_kotlin_flow15),
            FlowUsageBean(R.string.test_kotlin_flow16),
            FlowUsageBean(R.string.test_kotlin_flow17),
            FlowUsageBean(R.string.test_kotlin_flow18),
            FlowUsageBean(R.string.test_kotlin_flow19),
            FlowUsageBean(R.string.test_kotlin_flow20),
        )
    )

    val dataList = dataFlow.asLiveData()

}