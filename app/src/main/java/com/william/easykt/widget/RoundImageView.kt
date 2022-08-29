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

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.william.easykt.R


/**
 * author : WilliamYang
 * date : 2022/8/29 14:54
 * description : 带圆角图片
 *
 * 使用方式：
 *
 *    1. 使用 riv_radius 设置4个角均为圆角，且圆角值一样
 *
 *    2. 使用 riv_roundAsCircle 设置图片为圆形，使用 riv_radius 设置半径，当 riv_radius 未设置时，默认取宽高最小值的一半
 *
 *    3. 分别使用 riv_topLeft_radius, riv_topRight_radius, riv_bottomLeft_radius, riv_bottomRight_radius 设置4个圆角
 * <p>
 */
class RoundImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    /** 绘制路径 **/
    private val path = Path()

    /** 圆角大小 **/
    private var radius = 0f

    /** 顶部左侧圆角大小 **/
    private var topLeftRadius = 0f

    /** 顶部右侧圆角大小 **/
    private var topRightRadius = 0f

    /** 底部左侧圆角大小 **/
    private var bottomLeftRadius = 0f

    /** 底部右侧圆角大小 **/
    private var bottomRightRadius = 0f

    /** 作为圆形图片使用 **/
    private var roundAsCircle = false

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView)

        roundAsCircle = ta.getBoolean(R.styleable.RoundImageView_riv_roundAsCircle, false)

        radius = ta.getDimension(R.styleable.RoundImageView_riv_radius, 0f)

        topLeftRadius = ta.getDimension(R.styleable.RoundImageView_riv_topLeft_radius, 0f)
        topRightRadius = ta.getDimension(R.styleable.RoundImageView_riv_topRight_radius, 0f)
        bottomLeftRadius = ta.getDimension(R.styleable.RoundImageView_riv_bottomLeft_radius, 0f)
        bottomRightRadius = ta.getDimension(R.styleable.RoundImageView_riv_bottomRight_radius, 0f)

        ta.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // 当作为圆形图片使用，且半径未设置时，半径将取宽高最小值的一半
        if (roundAsCircle && radius <= 0f) {
            radius = w.coerceAtMost(h) / 2f
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 当作为圆形图片使用，宽高值不同，取宽高的最小值作为宽和高
        if (roundAsCircle) {
            val widthSize = MeasureSpec.getSize(widthMeasureSpec)
            val heightSize = MeasureSpec.getSize(heightMeasureSpec)
            if (widthSize > 0 && heightSize > 0 && widthSize != heightSize) {
                val size = widthSize.coerceAtMost(heightSize)
                setMeasuredDimension(size, size)
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (radius > 0 || topLeftRadius > 0 || topRightRadius > 0 || bottomLeftRadius > 0 || bottomRightRadius > 0) {
            // 如果设置了圆角值
            path.reset()
            if (roundAsCircle) {
                path.addCircle(radius, radius, radius, Path.Direction.CW)
            } else {
                if (topLeftRadius == 0f) topLeftRadius = radius
                if (topRightRadius == 0f) topRightRadius = radius
                if (bottomLeftRadius == 0f) bottomLeftRadius = radius
                if (bottomRightRadius == 0f) bottomRightRadius = radius

                val radii = floatArrayOf(
                    topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                    bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius
                )

                path.addRoundRect(
                    paddingLeft.toFloat(), paddingTop.toFloat(),
                    measuredWidth.toFloat() - paddingRight,
                    measuredHeight.toFloat() - paddingBottom,
                    radii, Path.Direction.CW
                )
            }

            // 裁剪画布
            canvas.clipPath(path)
        }

        super.onDraw(canvas)
    }

}