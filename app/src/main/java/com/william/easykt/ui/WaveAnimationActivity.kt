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

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Color
import android.graphics.Paint
import android.view.animation.LinearInterpolator
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.base_component.extension.dp
import com.william.easykt.databinding.ActivityWaveAnimationBinding


/**
 * @author William
 * @date 2020/9/2 19:16
 * Class Comment：
 */
class WaveAnimationActivity : BaseActivity() {
    override val viewBinding: ActivityWaveAnimationBinding by bindingView()

    override fun initAction() {

    }

    override fun initData() {
        setTitleText("WaveAnimationActivity")
        viewBinding.mWaveView.apply {
            setInitialRadius(15f.dp)
            setMaxRadiusRate(1f)
            setDuration(1500)
            setStyle(Paint.Style.FILL)
            setColor(Color.WHITE)
            setSpeed(1000)
            setInterpolator(LinearInterpolator())
            start()
        }

        viewBinding.start.setOnClickListener {
            viewBinding.mWaveView.start()
        }

        viewBinding.stop.setOnClickListener {
            viewBinding.mWaveView.stop()
        }

        viewBinding.stopImmediately.setOnClickListener {
            viewBinding.mWaveView.stopImmediately()
        }

        val animator = ObjectAnimator.ofPropertyValuesHolder(
            viewBinding.viewOuter,
            PropertyValuesHolder.ofFloat("scaleX", 2f),
            PropertyValuesHolder.ofFloat("scaleY", 2f),
            PropertyValuesHolder.ofFloat("alpha", 0f)
        ).apply {
            repeatCount = ObjectAnimator.INFINITE
            duration = 1000
            start()
        }

        val animator2 = ObjectAnimator.ofPropertyValuesHolder(
            viewBinding.viewOuter2,
            PropertyValuesHolder.ofFloat("scaleX", 2f),
            PropertyValuesHolder.ofFloat("scaleY", 2f),
            PropertyValuesHolder.ofFloat("alpha", 0f)
        ).apply {
            repeatCount = ObjectAnimator.INFINITE
            duration = 1000
            start()
        }

    }
}