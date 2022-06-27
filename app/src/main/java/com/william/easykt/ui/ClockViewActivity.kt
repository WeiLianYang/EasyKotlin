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
import com.william.easykt.databinding.ActivityClockViewBinding


/**
 * @author William
 * @date 2022/6/27 21:49
 * Class Comment：自定义闹钟
 */
class ClockViewActivity : BaseActivity() {

    override val viewBinding: ActivityClockViewBinding by bindingView()

    override fun initData() {
        viewBinding.clock1.performAnimation()

        viewBinding.clock2.performAnimation()
    }
}