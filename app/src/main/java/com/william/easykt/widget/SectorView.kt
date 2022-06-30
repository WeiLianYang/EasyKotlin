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
import android.view.animation.AccelerateInterpolator
import com.william.base_component.extension.dp
import com.william.base_component.extension.logV
import com.william.easykt.data.SectorBean

/**
 * author : WilliamYang
 * date : 2022/6/30 11:43
 * description : 多彩扇形
 */
class SectorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    private var centerX = 0
    private var centerY = 0

    private var circleRect = RectF()

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val dataList = arrayListOf<SectorBean>()

    private var animator: ValueAnimator? = null
    private var animatedValue = 0f
    private val defaultDimension = 50.dp

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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val sideLength = w.coerceAtMost(h)
        val radius = sideLength / 2

        centerX = w / 2
        centerY = h / 2

        val strokeWidth = radius * 0.3f
        paint.strokeWidth = strokeWidth

        val rectSide = radius - strokeWidth / 2
        circleRect[-rectSide, -rectSide, rectSide] = rectSide
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        canvas.translate(centerX.toFloat(), centerY.toFloat())

        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        var startAngle = 0
        for (chartBean in dataList) {
            val sweepAngle = chartBean.percentage
            val drawAngle = sweepAngle.coerceAtMost(animatedValue - startAngle)
            if (drawAngle > 0) {
                paint.color = chartBean.color
                canvas.drawArc(circleRect, startAngle.toFloat(), drawAngle, false, paint)
            }
            startAngle += sweepAngle.toInt()
        }

        canvas.restore()
    }

    /**
     * 设置数据
     *
     * @param list 扇形数据
     */
    fun setData(list: List<SectorBean>?) {
        if (list.isNullOrEmpty()) {
            return
        }
        dataList.clear()
        dataList.addAll(list)
    }

    /**
     * 设置进度条动画
     *
     * @param duration 动画时长
     */
    fun startAnimation(duration: Long) {
        if (animator?.isRunning == true) {
            animator?.cancel()
        } else {
            animator = ValueAnimator
                .ofFloat(0f, 360f)
                .setDuration(duration).also {
                    it?.interpolator = AccelerateInterpolator()
                    it?.addUpdateListener { animation ->
                        /**每次要绘制的圆弧角度 */
                        animatedValue = animation.animatedValue as Float
                        invalidate()
                    }
                }
        }
        animator?.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
        "sector view detached from window".logV()
    }

}