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
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.william.base_component.extension.logD

/**
 * author：William
 * date：2022/5/28 22:57
 * description：用于展示 Camera 2d 变换的demo
 */
class Camera2DImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(
    context,
    attrs,
    defStyleAttr
) {
    //    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val camera = Camera()
    private val mMatrix = Matrix()

    private var progressX = 0f
    private var progressY = 0f
    private var progressZ = 0f

    var action = 0

    fun setProgress(progressX: Int, progressY: Int, progressZ: Int) {
        "progressX: $progressX, progressY: $progressY, progressZ: $progressZ".logD()
        this.progressX = progressX.toFloat()
        this.progressY = progressY.toFloat()
        this.progressZ = progressZ.toFloat()
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        camera.save()
        canvas.save()

        if (action == 0) {
            camera.rotate(0f, 0f, 0f)
            camera.translate(progressX, progressY, progressZ)
        } else {
            camera.translate(0f, 0f, 0f)
            camera.rotate(progressX, progressY, progressZ)
        }

        camera.getMatrix(mMatrix)

        // 调节中心点
        val centerX = width / 2f
        val centerY = height / 2f
        "centerX: $centerX, centerY: $centerY".logD()
        mMatrix.preTranslate(-centerX, -centerY)
        mMatrix.postTranslate(centerX, centerY)

        // 将 Camera 位置移动到 源图像 中心点 位置
        /*
        val centerX = width / 2f / 72
        val centerY = height / 2f / 72
        camera.setLocation(centerX, -centerY, camera.locationZ)
        */

        canvas.setMatrix(mMatrix)
        camera.restore()

        super.onDraw(canvas)
        canvas.restore()
    }
}