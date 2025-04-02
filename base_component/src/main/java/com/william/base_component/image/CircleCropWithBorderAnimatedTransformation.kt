package com.william.base_component.image

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import coil.annotation.ExperimentalCoilApi
import coil.transform.AnimatedTransformation
import coil.transform.PixelOpacity

/**
 * ### author : WilliamYang
 * ### date : 2025/4/1 18:01
 * ### description : 设置动画图像变换为圆形，添加外框
 */
@OptIn(ExperimentalCoilApi::class)
class CircleCropWithBorderAnimatedTransformation(
    private val borderSize: Float, private val borderColor: Int
) : AnimatedTransformation {

    @OptIn(ExperimentalCoilApi::class)
    override fun transform(canvas: Canvas): PixelOpacity {
        val path = Path().apply {
            fillType = Path.FillType.INVERSE_EVEN_ODD
        }
        val width = canvas.width.toFloat()
        val height = canvas.height.toFloat()
        val minSize = minOf(width, height)
        val radius = minSize / 2
        path.addCircle(width / 2, height / 2, radius, Path.Direction.CW)

        val paint = Paint().apply {
            isAntiAlias = true
            color = Color.TRANSPARENT
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
        }
        canvas.drawPath(path, paint)

        // draw border
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = borderColor
            style = Paint.Style.STROKE
            strokeWidth = borderSize
        }
        canvas.drawCircle(width / 2, height / 2, radius - borderSize / 2, borderPaint)

        return PixelOpacity.TRANSLUCENT
    }

}