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
import android.graphics.Color
import android.graphics.Path
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.william.base_component.extension.logD
import com.william.easykt.R


/**
 * @author William
 * @date 2022/4/25 15:08
 * Class Comment：
 */
class RoundCornerLayout : ConstraintLayout {

    private val path = Path()
    private var bgColor: String? = null

    private val noneVal = -1f
    private val defaultRadius = 0f

    private var radius = defaultRadius

    private var topLeftRadius = noneVal
    private var topRightRadius = noneVal
    private var bottomLeftRadius = noneVal
    private var bottomRightRadius = noneVal

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerLayout)
        radius = ta.getDimension(R.styleable.RoundCornerLayout_corner_radius, defaultRadius)

        topLeftRadius = ta.getDimension(R.styleable.RoundCornerLayout_topLeft_radius, noneVal)
        topRightRadius = ta.getDimension(R.styleable.RoundCornerLayout_topRight_radius, noneVal)
        bottomLeftRadius = ta.getDimension(R.styleable.RoundCornerLayout_bottomLeft_radius, noneVal)
        bottomRightRadius =
            ta.getDimension(R.styleable.RoundCornerLayout_bottomRight_radius, noneVal)

        ta.recycle()
    }

    fun setCornerRadius(radius: Int) {
        this.radius = radius.toFloat()
    }

    fun setSpecialCorner(leftTop: Int, rightTop: Int, leftBottom: Int, rightBottom: Int) {
        topLeftRadius = leftTop.toFloat()
        topRightRadius = rightTop.toFloat()
        bottomLeftRadius = leftBottom.toFloat()
        bottomRightRadius = rightBottom.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        if (TextUtils.isEmpty(bgColor)) {
            val bgDrawable = background
            if (bgDrawable is ColorDrawable) {
                val color = bgDrawable.color
                bgColor = "#" + String.format("%08x", color)
                "color: $color, bgColor: $bgColor".logD()
            }
        }
        setBackgroundColor(Color.parseColor("#00FFFFFF"))

        super.onDraw(canvas)
    }

    override fun dispatchDraw(canvas: Canvas) {
        val width = measuredWidth.toFloat()
        val height = measuredHeight.toFloat()

        topLeftRadius = if (topLeftRadius == noneVal) radius else topLeftRadius
        topRightRadius = if (topRightRadius == noneVal) radius else topRightRadius
        bottomLeftRadius = if (bottomLeftRadius == noneVal) radius else bottomLeftRadius
        bottomRightRadius = if (bottomRightRadius == noneVal) radius else bottomRightRadius

        val radii = floatArrayOf(
            topLeftRadius,
            topLeftRadius,
            topRightRadius,
            topRightRadius,
            bottomRightRadius,
            bottomRightRadius,
            bottomLeftRadius,
            bottomLeftRadius
        )

        path.reset()
        path.addRoundRect(paddingLeft.toFloat(), paddingTop.toFloat(), width - paddingRight, height - paddingBottom, radii, Path.Direction.CW)

        canvas.save()
        canvas.clipPath(path)

        if (!TextUtils.isEmpty(bgColor)) {
            canvas.drawColor(Color.parseColor(bgColor))
        }

        super.dispatchDraw(canvas)
        canvas.restore()
    }

}