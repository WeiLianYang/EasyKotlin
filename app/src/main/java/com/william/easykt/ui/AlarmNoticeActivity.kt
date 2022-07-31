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
import com.william.base_component.extension.bindingView
import com.william.base_component.extension.logI
import com.william.easykt.databinding.ActivityAlarmNoticeBinding
import com.william.easykt.ui.AlarmManagerActivity.Companion.KEY1
import com.william.easykt.ui.AlarmManagerActivity.Companion.KEY2


/**
 * @author William
 * @date 2022/7/31 10:50
 * Class Comment：闹钟提示
 */
class AlarmNoticeActivity : BaseActivity() {

    override val viewBinding: ActivityAlarmNoticeBinding by bindingView()

    override fun initData() {
        val key1 = intent.getIntExtra(KEY1, -1)
        val key2 = intent.getBooleanExtra(KEY2, false)
        "alarm notice, key1: $key1, key2: $key2".logI()
    }


}