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

package com.william.base_component.widgets

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.roundToInt

/**
 * author : WilliamYang
 * date : 2021/7/28 16:35
 * description : 波形视图
 */
class WaveAnimatorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    View(context, attrs), View.OnClickListener {

    private var mPath = Path()
    private var mPath2 = Path()

    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mPaint2: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mWidth = 0
    private var mHeight = 0

    // 振幅：波谷/波峰 距中心点位置距离
    var amplitude = 50

    // 波长：相邻两个 波峰/波谷 之间的距离
    val waveLength = 800f

    // 波的数量
    private var waveCount = 0

    // 波的中心点位置
    private var centerY = 0f

    // 波的偏移量
    private var offset = 0f

    // 波的偏移量
    private var offset2 = 0f

    private var animator: ValueAnimator? = null

    private var animator2: ValueAnimator? = null

    private var mStartColor = Color.RED
    private var mEndColor = Color.YELLOW

    init {
        initPaint()
    }

    private fun initPaint() {
        mPaint.apply {
            color = Color.BLUE
            style = Paint.Style.FILL
        }
        mPaint2.apply {
            color = Color.GREEN
            style = Paint.Style.FILL
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        centerY = h / 2f
        waveCount = (mWidth / waveLength + 2.5).roundToInt()

        val gradient = LinearGradient(
            0f,
            centerY,
            0f,
            mHeight * 1f,
            mStartColor,
            mEndColor,
            Shader.TileMode.CLAMP
        )
        mPaint2.shader = gradient
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPath.apply {
            reset()
            // 重置后，移动到屏幕左侧一个波长位置处
            moveTo((-waveLength + offset), centerY)
        }
        mPath2.apply {
            reset()
            moveTo((-waveLength * 5 / 4 + offset2), centerY)
        }
        for (index in 0 until waveCount) {
            val cycle = index * waveLength + offset
            val cycle2 = index * waveLength + offset2
            mPath.apply {
                // 先画屏幕左侧第一个波峰，终点为屏幕左侧波长的1/2
                quadTo(
                    -waveLength * 3 / 4 + cycle, centerY - amplitude,
                    -waveLength / 2 + cycle, centerY
                )
                // 再画屏幕左侧第一个波谷，终点为屏幕左侧边缘
                quadTo(
                    -waveLength / 4 + cycle, centerY + amplitude, cycle, centerY
                )
            }
            mPath2.apply {
                quadTo(
                    -waveLength + cycle2, centerY - amplitude,
                    -waveLength * 3 / 4 + cycle2, centerY
                )
                quadTo(
                    -waveLength / 2 + cycle2, centerY + amplitude,
                    -waveLength / 4 + cycle2, centerY
                )
            }
        }
        mPath.apply {
            lineTo(mWidth.toFloat(), mHeight.toFloat())
            lineTo(0f, mHeight.toFloat())
            close()
        }
        mPath2.apply {
            lineTo(mWidth.toFloat(), mHeight.toFloat())
            lineTo(0f, mHeight.toFloat())
            close()
        }

        canvas.drawPath(mPath, mPaint)

        canvas.drawPath(mPath2, mPaint2)

        setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (animator?.isRunning == true) {
            animator?.cancel()
        } else {
            animator?.start()
        }
        if (animator == null) {
            animator = ValueAnimator.ofFloat(0f, waveLength)?.apply {
                duration = 1000
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                addUpdateListener { valueAnimator: ValueAnimator ->
                    offset = valueAnimator.animatedValue as Float
                    invalidate()
                }
                start()
            }
        }
        if (animator2?.isRunning == true) {
            animator2?.cancel()
        } else {
            animator2?.start()
        }
        if (animator2 == null) {
            animator2 = ValueAnimator.ofFloat(0f, waveLength)?.apply {
                duration = 1500
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                addUpdateListener { valueAnimator: ValueAnimator ->
                    offset2 = valueAnimator.animatedValue as Float
                }
                start()
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
        animator2?.cancel()
    }

}