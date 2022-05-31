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

package com.william.easykt.ui.camera

import com.william.base_component.extension.bindingView
import com.william.base_component.fragment.BaseFragment
import com.william.easykt.databinding.FragmentCamera3dClockBinding


/**
 * @author William
 * @date 2022/5/29 22:32
 * Class Comment：clock demo use camera
 */
class CameraClockFragment : BaseFragment() {

    override val viewBinding by bindingView<FragmentCamera3dClockBinding>()

    override fun initAction() {
    }

    override fun sendRequest() {
    }
}