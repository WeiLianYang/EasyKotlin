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
import java.lang.Integer.min


/**
 * author : WilliamYang
 * date : 2022/8/25 17:17
 * description : 圆形布局
 */
class CircleLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val path = Path()
    private var bgColor: String? = null

    private val defaultRadius = 0f
    private var radius = defaultRadius

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CircleLayout)
        radius = ta.getDimension(R.styleable.CircleLayout_circle_radius, defaultRadius)

        ta.recycle()
    }

    fun setCornerRadius(radius: Int) {
        this.radius = radius.toFloat()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
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
        if (radius == 0f) {
            radius = min(measuredWidth, measuredHeight) / 2f
        }
        if (radius > 0) {
            path.reset()
            path.addCircle(radius, radius, radius, Path.Direction.CW)

            canvas.save()
            canvas.clipPath(path)

            "canvas clipPath".logD()
        }
        if (!TextUtils.isEmpty(bgColor)) {
            "draw color $bgColor".logD()
            canvas.drawColor(Color.parseColor(bgColor))
        }

        super.dispatchDraw(canvas)
        canvas.restore()
    }

}