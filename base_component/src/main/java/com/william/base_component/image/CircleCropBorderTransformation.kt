package com.william.base_component.image

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import coil.size.Size
import coil.transform.Transformation

/**
 * ### author : WilliamYang
 * ### date : 2022/11/1 9:47
 * ### description : Circle Crop $ Border
 */
class CircleCropBorderTransformation(private val borderSize: Int, private val borderColor: Int) :
    Transformation {

    init {
        require(borderSize >= 0) {
            "borderSize must be >= 0."
        }
    }

    override val cacheKey = "${javaClass.name}-$borderSize,$borderColor"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

        val minSize = minOf(input.width, input.height)
        val radius = minSize / 2f
        val output = createBitmap(minSize, minSize, input.config ?: Bitmap.Config.ARGB_8888)
        output.applyCanvas {
            drawCircle(radius, radius, radius, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            drawBitmap(input, radius - input.width / 2f, radius - input.height / 2f, paint)

            val maxSize = maxOf(input.width, input.height)
            val strokeSize = borderSize * 1f * minSize / maxSize
            val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = borderColor
                style = Paint.Style.STROKE
                strokeWidth = strokeSize
            }
            drawCircle(radius, radius, radius - strokeSize / 2, borderPaint)
        }
        return output
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is CircleCropBorderTransformation &&
                borderSize == other.borderSize &&
                borderColor == other.borderColor
    }

    override fun hashCode(): Int {
        return javaClass.hashCode() + borderSize.hashCode() + borderColor.hashCode()
    }

}