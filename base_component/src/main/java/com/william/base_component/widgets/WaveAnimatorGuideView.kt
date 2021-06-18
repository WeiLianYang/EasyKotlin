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

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator


/**
 * @author William
 * @date 2020-02-07 14:30
 * Class Comment：水波纹效果
 */
class WaveAnimatorGuideView : View {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var mInitialRadius = 0f// 初始波纹半径
    private var mMaxRadius = 0f// 最大波纹半径
    private var mDuration: Long = 2000 // 一个波纹从创建到消失的持续时间
    private var mSpeed = 800 // 波纹的创建速度，每500ms创建一个
    private var mMaxRadiusRate = 0.85f
    private var mMaxRadiusSet = false
    private val maxAlpha = 100

    private var mIsRunning = false
    private var mLastCreateTime: Long = 0
    private val mCircleList: MutableList<Circle> = ArrayList()

    private val mCreateCircle: Runnable = object : Runnable {
        override fun run() {
            if (mIsRunning) {
                newCircle()
                postDelayed(this, mSpeed.toLong())
            }
        }
    }

    private var mInterpolator: Interpolator? = LinearInterpolator()

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    fun setStyle(style: Paint.Style?) {
        mPaint.style = style
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (!mMaxRadiusSet) {
            mMaxRadius = w.coerceAtMost(h) * mMaxRadiusRate / 2.0f
        }
    }

    fun setMaxRadiusRate(maxRadiusRate: Float) {
        mMaxRadiusRate = maxRadiusRate
    }

    fun setColor(color: Int) {
        mPaint.color = color
    }

    /**
     * 开始
     */
    fun start() {
        if (!mIsRunning) {
            mIsRunning = true
            mCreateCircle.run()
        }
    }

    /**
     * 缓慢停止
     */
    fun stop() {
        mIsRunning = false
    }

    /**
     * 立即停止
     */
    fun stopImmediately() {
        mIsRunning = false
        mCircleList.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val iterator = mCircleList.iterator()
        while (iterator.hasNext()) {
            val circle = iterator.next()
            val radius = circle.currentRadius
            if (System.currentTimeMillis() - circle.mCreateTime < mDuration) {
                mPaint.alpha = circle.alpha
                canvas.drawCircle(width / 2.toFloat(), height / 2.toFloat(), radius, mPaint)
            } else {
                iterator.remove()
            }
        }
        if (mCircleList.size > 0) {
            postInvalidateDelayed(10)
        }
    }

    fun setInitialRadius(radius: Float) {
        mInitialRadius = radius
    }

    fun setDuration(duration: Long) {
        mDuration = duration
    }

    fun setMaxRadius(maxRadius: Float) {
        mMaxRadius = maxRadius
        mMaxRadiusSet = true
    }

    fun setSpeed(speed: Int) {
        mSpeed = speed
    }

    private fun newCircle() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - mLastCreateTime < mSpeed) {
            return
        }
        val circle = Circle()
        mCircleList.add(circle)
        invalidate()
        mLastCreateTime = currentTime
    }

    private inner class Circle {
        val mCreateTime: Long = System.currentTimeMillis()
        val alpha: Int
            get() {
                val percent: Float =
                    (currentRadius - mInitialRadius) / (mMaxRadius - mInitialRadius)
                return (maxAlpha - (mInterpolator?.getInterpolation(percent)
                    ?: percent) * maxAlpha).toInt()
            }
        val currentRadius: Float
            get() {
                val percent: Float = (System.currentTimeMillis() - mCreateTime) * 1.0f / mDuration
                return mInitialRadius + (mInterpolator?.getInterpolation(percent)
                    ?: percent) * (mMaxRadius - mInitialRadius)
            }

    }

    fun setInterpolator(interpolator: Interpolator?) {
        mInterpolator = interpolator
        if (mInterpolator == null) {
            mInterpolator = LinearInterpolator()
        }
    }
}