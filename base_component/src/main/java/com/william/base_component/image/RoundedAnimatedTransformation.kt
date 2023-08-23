package com.william.base_component.image

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.annotation.Px
import coil.annotation.ExperimentalCoilApi
import coil.transform.AnimatedTransformation
import coil.transform.PixelOpacity

/**
 * ### author : WilliamYang
 * ### date : 2023/8/23 14:02
 * ### description : 设置动画图像变换为圆角矩形
 */
@OptIn(ExperimentalCoilApi::class)
class RoundedAnimatedTransformation(
    @Px private val topLeft: Float = 0f,
    @Px private val topRight: Float = 0f,
    @Px private val bottomLeft: Float = 0f,
    @Px private val bottomRight: Float = 0f
) : AnimatedTransformation {

    constructor(@Px radius: Float) : this(radius, radius, radius, radius)

    init {
        require(topLeft >= 0 && topRight >= 0 && bottomLeft >= 0 && bottomRight >= 0) {
            "All radii must be >= 0."
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    override fun transform(canvas: Canvas): PixelOpacity {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
            color = Color.TRANSPARENT
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
        }
        val path = Path().apply {
            fillType = Path.FillType.INVERSE_EVEN_ODD
        }

        val width = canvas.width.toFloat()
        val height = canvas.height.toFloat()

        val radii = floatArrayOf(
            topLeft, topLeft,
            topRight, topRight,
            bottomRight, bottomRight,
            bottomLeft, bottomLeft,
        )
        path.addRoundRect(0f, 0f, width, height, radii, Path.Direction.CW)
        canvas.drawPath(path, paint)

        return PixelOpacity.TRANSLUCENT
    }
}