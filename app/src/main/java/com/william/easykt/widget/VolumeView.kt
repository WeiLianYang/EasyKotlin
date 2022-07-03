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

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import com.william.base_component.extension.dp

/**
 * author：William
 * date：2022/7/3 14:51
 * description：volume circle view
 */
class VolumeView @JvmOverloads constructor(
    context: Context, @Nullable attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val backgroundColor = 0x60000000
    private var borderWidth = 0f
    private val volumeBgColor = -0x80000000
    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f
    private var volumeRadius = 0f
    private var volumeRect = RectF()
    private val volumeColor = Color.GREEN
    private var currentVolume = 0
    private val maxVolume = 10
    private val unitDegree = 360 / maxVolume
    private var animatedDegree = 0
    private var defaultDimension = 0
    private var volumeUp = true

    init {
        borderWidth = 8f.dp
        defaultDimension = 150.dp
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        radius = w.coerceAtMost(h) / 2f
        volumeRadius = radius - borderWidth

        centerX = w / 2f
        centerY = h / 2f

        volumeRect = RectF(-volumeRadius, -volumeRadius, volumeRadius, volumeRadius)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultDimension, defaultDimension)
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultDimension, heightSize)
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, defaultDimension)
        } else {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        canvas.translate(centerX, centerY)

        paint.apply {
            color = backgroundColor
            style = Paint.Style.FILL
        }

        // draw circle bg
        canvas.drawCircle(0f, 0f, radius, paint)

        paint.apply {
            color = volumeBgColor
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
        }

        // draw volume bg
        canvas.drawCircle(0f, 0f, volumeRadius, paint)
        paint.color = volumeColor

        // draw volume
        if (volumeUp) {
            val volume = if (currentVolume - 1 > 0) currentVolume - 1 else 0
            canvas.drawArc(
                volumeRect, -90f, (unitDegree * volume + animatedDegree).toFloat(), false, paint
            )
        } else {
            val num = currentVolume + 1
            canvas.drawArc(
                volumeRect, -90f, (unitDegree * num - animatedDegree).toFloat(), false, paint
            )
        }

        canvas.restore()
    }

    /**
     * volume up
     */
    fun volumeUp() {
        volumeUp = true
        if (currentVolume < maxVolume) {
            currentVolume++
            startAnim()
        }
    }

    /**
     * reduce volume
     */
    fun volumeDown() {
        volumeUp = false
        if (currentVolume > 0) {
            currentVolume--
            startAnim()
        }
    }

    /**
     * control volume with animation
     */
    private fun startAnim() {
        val valueAnimator = ValueAnimator.ofInt(0, unitDegree)
        valueAnimator.duration = 300
        valueAnimator.addUpdateListener { animation ->
            animatedDegree = animation.animatedValue as Int
            invalidate()
        }
        valueAnimator.start()
    }

}