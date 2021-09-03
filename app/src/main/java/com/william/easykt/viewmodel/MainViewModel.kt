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
import com.william.easykt.data.MainEntranceBean
import kotlinx.coroutines.flow.flowOf


/**
 * @author William
 * @date 2021/8/27 22:19
 * Class Comment：
 */
class MainViewModel : ViewModel() {

    private val dataFlow = flowOf(
        listOf(
            MainEntranceBean(R.string.test_net_data),
            MainEntranceBean(R.string.test_card_effects),
            MainEntranceBean(R.string.test_wave_effects),
            MainEntranceBean(R.string.test_card_pager),
            MainEntranceBean(R.string.test_scroll),
            MainEntranceBean(R.string.test_permission),
            MainEntranceBean(R.string.test_open_target_page),
            MainEntranceBean(R.string.test_available_space),
            MainEntranceBean(R.string.test_flurry_analytics),
            MainEntranceBean(R.string.test_flurry_crash),
            MainEntranceBean(R.string.test_nested_scrolling),
            MainEntranceBean(R.string.test_kotlin_flow),
            MainEntranceBean(R.string.test_bottom_sheet),
            MainEntranceBean(R.string.test_kotlin_channel),
            MainEntranceBean(R.string.test_material_snackbar),
        )
    )

    val dataList = dataFlow.asLiveData()

}