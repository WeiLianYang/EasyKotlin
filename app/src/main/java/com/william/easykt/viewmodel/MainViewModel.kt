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
            MainEntranceBean(R.string.test_diff_util),
            MainEntranceBean(R.string.test_kotlin_coroutines),
            MainEntranceBean(R.string.test_activity_result),
            MainEntranceBean(R.string.test_file),
            MainEntranceBean(R.string.test_notification),
            MainEntranceBean(R.string.test_package_visibility),
            MainEntranceBean(R.string.test_jetpack_compose1),
            MainEntranceBean(R.string.test_jetpack_compose2),
            MainEntranceBean(R.string.test_jetpack_compose3),
            MainEntranceBean(R.string.test_window_insets),
            MainEntranceBean(R.string.test_aes_encrypt),
            MainEntranceBean(R.string.test_bubble),
            MainEntranceBean(R.string.test_touch_scale_image),
            MainEntranceBean(R.string.test_round_rect),
            MainEntranceBean(R.string.test_motion_layout),
            MainEntranceBean(R.string.test_camerax),
            MainEntranceBean(R.string.test_side_slip),
            MainEntranceBean(R.string.test_drawer),
            MainEntranceBean(R.string.test_web),
            MainEntranceBean(R.string.test_camera_demo),
            MainEntranceBean(R.string.test_clock_view),
        )
    )

    val dataList = dataFlow.asLiveData()

}