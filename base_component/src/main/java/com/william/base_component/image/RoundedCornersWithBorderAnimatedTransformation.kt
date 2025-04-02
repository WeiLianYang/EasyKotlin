package com.william.base_component.image

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.annotation.ColorInt
import androidx.annotation.Px
import coil.annotation.ExperimentalCoilApi
import coil.transform.AnimatedTransformation
import coil.transform.PixelOpacity

/**
 * ### author : WilliamYang
 * ### date : 2025/4/2 9:08
 * ### description : 设置动画图像变换为圆角矩形，并添加外框
 */
@OptIn(ExperimentalCoilApi::class)
class RoundedCornersWithBorderAnimatedTransformation(
    @Px private val topLeft: Float = 0f,
    @Px private val topRight: Float = 0f,
    @Px private val bottomLeft: Float = 0f,
    @Px private val bottomRight: Float = 0f,
    @Px private val borderSize: Float = 0f,
    @ColorInt private val borderColor: Int,
) : AnimatedTransformation {

    constructor(@Px radius: Float, @Px borderSize: Float, @ColorInt borderColor: Int) : this(
        radius, radius, radius, radius, borderSize, borderColor
    )

    init {
        require(topLeft >= 0 && topRight >= 0 && bottomLeft >= 0 && bottomRight >= 0) {
            "All radii must be >= 0."
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    override fun transform(canvas: Canvas): PixelOpacity {
        val width = canvas.width.toFloat()
        val height = canvas.height.toFloat()

        val radii = floatArrayOf(
            topLeft, topLeft,
            topRight, topRight,
            bottomRight, bottomRight,
            bottomLeft, bottomLeft,
        )

        val halfBorderWidth = borderSize / 2
        val borderPath = Path()
        borderPath.addRoundRect(
            halfBorderWidth,
            halfBorderWidth,
            width - halfBorderWidth,
            height - halfBorderWidth,
            radii,
            Path.Direction.CW
        )
        if (halfBorderWidth > 0) {
            radii.forEachIndexed { index, f ->
                if (f > 0) {
                    radii[index] = f + halfBorderWidth
                }
            }
        }

        // draw corner
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
            color = Color.TRANSPARENT
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
        }
        val path = Path().apply {
            fillType = Path.FillType.INVERSE_EVEN_ODD
        }
        path.addRoundRect(0f, 0f, width, height, radii, Path.Direction.CW)
        canvas.drawPath(path, paint)

        // draw border
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
            color = borderColor
            style = Paint.Style.STROKE
            strokeWidth = borderSize
        }
        canvas.drawPath(borderPath, borderPaint)

        return PixelOpacity.TRANSLUCENT
    }

}