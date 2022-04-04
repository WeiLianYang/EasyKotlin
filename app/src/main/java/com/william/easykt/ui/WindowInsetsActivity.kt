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

import android.os.CancellationSignal
import android.view.animation.LinearInterpolator
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationControlListenerCompat
import androidx.core.view.WindowInsetsAnimationControllerCompat
import androidx.core.view.WindowInsetsCompat
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.base_component.extension.logD
import com.william.base_component.extension.logI
import com.william.base_component.extension.logW
import com.william.easykt.databinding.ActivityWindowInsetsBinding
import com.william.easykt.ui.adapter.SimpleAdapter


/**
 * @author William
 * @date 2022/3/27 11:15
 * Class Comment：unfinished demo
 */
class WindowInsetsActivity : BaseActivity() {

    override val viewBinding: ActivityWindowInsetsBinding by bindingView()

    override fun initView() {
    }

    override fun initAction() {
    }

    override fun initData() {
        setTitleText("WindowInsetsActivity")

        viewBinding.recyclerView.adapter = SimpleAdapter()

        ViewCompat.getWindowInsetsController(viewBinding.edittext)?.controlWindowInsetsAnimation(
            WindowInsetsCompat.Type.ime(), -1, LinearInterpolator(), CancellationSignal(),
            object : WindowInsetsAnimationControlListenerCompat {

                override fun onReady(
                    controller: WindowInsetsAnimationControllerCompat,
                    types: Int
                ) {
                    "onReady, types: $types".logD()
                }

                override fun onFinished(controller: WindowInsetsAnimationControllerCompat) {
                    "onFinished".logI()
                }

                override fun onCancelled(controller: WindowInsetsAnimationControllerCompat?) {
                    "onCancelled".logW()
                }
            }
        )
    }
}