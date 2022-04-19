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

package com.william.easykt.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import com.william.base_component.extension.logD
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * 多指触摸缩放 ImageView
 */
class TouchScaleImageView : AppCompatImageView {
    private var count = 0
    private var oldDistance = 0f
    private var scale = 1f
    private val minScale = 0.3f
    private val maxScale = 2f
    private var lastTime = 0L

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                oldDistance = 0f
                count = 1
            }
            MotionEvent.ACTION_UP -> count = 0
            MotionEvent.ACTION_POINTER_UP -> count -= 1
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDistance = computeSpacing(event)
                count += 1
            }
            MotionEvent.ACTION_MOVE -> if (count >= 2) {
                val newDistance = computeSpacing(event)
                if (abs(newDistance - oldDistance) > 100 && System.currentTimeMillis() - lastTime > 500) {
                    lastTime = System.currentTimeMillis()
                    "newDistance: $newDistance, oldDistance: $oldDistance".logD()
                    zoom(newDistance / oldDistance)
                    oldDistance = newDistance
                }
            }
        }
        return true
    }

    private fun zoom(factor: Float) {
        "factor: $factor".logD()
        scale *= factor

        if (scale < minScale) {
            scale = minScale
        } else if (scale > maxScale) {
            scale = maxScale
        }

        "scale: $scale".logD()

        scaleX = scale
        scaleY = scale
    }

    private fun computeSpacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt((x * x + y * y).toDouble()).toFloat()
    }
}