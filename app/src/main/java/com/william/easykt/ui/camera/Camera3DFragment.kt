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

import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.william.base_component.extension.bindingView
import com.william.base_component.fragment.BaseFragment
import com.william.easykt.databinding.FragmentCamera3dBinding
import com.william.easykt.ui.anim.CameraRotateAnimation


/**
 * @author William
 * @date 2022/5/29 22:32
 * Class Comment：Camera 3d transformations
 */
class Camera3DFragment : BaseFragment() {

    override val viewBinding by bindingView<FragmentCamera3dBinding>()

    private val mDuration = 600L
    private var openAnimation: CameraRotateAnimation? = null
    private var closeAnimation: CameraRotateAnimation? = null

    private var reverse = false

    override fun initAction() {
        initOpenAnim()
        initCloseAnim()

        viewBinding.btnReverse.setOnClickListener {
            reverse()
        }
    }

    private fun initOpenAnim() {
        openAnimation = CameraRotateAnimation(0f, 90f, true).apply {
            duration = mDuration
            fillAfter = true
            interpolator = AccelerateInterpolator()

            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {
                    viewBinding.ivLogo.isGone = true
                    viewBinding.ivLogo2.isVisible = true

                    val rotateAnimation = CameraRotateAnimation(90f, 180f, false).apply {
                        duration = mDuration
                        fillAfter = true
                        interpolator = DecelerateInterpolator()
                    }
                    viewBinding.clContent.startAnimation(rotateAnimation)
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
    }

    private fun initCloseAnim() {
        closeAnimation = CameraRotateAnimation(180f, 90f, true).apply {
            duration = mDuration
            fillAfter = true
            interpolator = AccelerateInterpolator()
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {
                    viewBinding.ivLogo.isVisible = true
                    viewBinding.ivLogo2.isGone = true
                    val rotateAnimation = CameraRotateAnimation(90f, 0f, false).apply {
                        duration = mDuration
                        fillAfter = true
                        interpolator = DecelerateInterpolator()
                    }
                    viewBinding.clContent.startAnimation(rotateAnimation)
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
    }

    private fun reverse() {
        if (openAnimation?.hasStarted() == true && openAnimation?.hasEnded() == false) {
            return
        }
        if (closeAnimation?.hasStarted() == true && closeAnimation?.hasEnded() == false) {
            return
        }
        if (reverse) {
            viewBinding.clContent.startAnimation(closeAnimation)
        } else {
            viewBinding.clContent.startAnimation(openAnimation)
        }
        reverse = !reverse
    }
}