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
import com.william.base_component.extension.logD
import java.lang.Integer.min


/**
 * author : WilliamYang
 * date : 2022/8/25 17:17
 * description : 圆形布局
 */
class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        val radius = min(measuredWidth, measuredHeight) / 2f
        if (radius > 0) {
            path.reset()
            path.addCircle(radius, radius, radius, Path.Direction.CW)

            canvas.clipPath(path)
            "canvas clipPath".logD()
        }
        super.onDraw(canvas)
    }

}