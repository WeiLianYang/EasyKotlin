package com.william.base_component.image

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.Shader
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import coil.decode.DecodeUtils
import coil.size.Dimension
import coil.size.Scale
import coil.size.Size
import coil.size.isOriginal
import coil.size.pxOrElse
import coil.transform.Transformation
import kotlin.math.roundToInt

/**
 * ### author : WilliamYang
 * ### date : 2023/12/21 10:34
 * ### description : Rounded Corner & Border
 */
class RoundedCornersWithBorderTransformation(
    @Px private val topLeft: Float = 0f,
    @Px private val topRight: Float = 0f,
    @Px private val bottomLeft: Float = 0f,
    @Px private val bottomRight: Float = 0f,
    @Px private val borderSize: Int = 0,
    @ColorInt private val borderColor: Int
) : Transformation {

    constructor(@Px radius: Float, @Px borderSize: Int, @ColorInt borderColor: Int) : this(
        radius, radius, radius, radius,
        borderSize, borderColor
    )

    init {
        require(topLeft >= 0 && topRight >= 0 && bottomLeft >= 0 && bottomRight >= 0 && borderSize >= 0) {
            "All radii and border size must be >= 0."
        }
    }

    override val cacheKey =
        "${javaClass.name}-$topLeft,$topRight,$bottomLeft,$bottomRight,$borderSize,$borderColor"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

        val (outputWidth, outputHeight) = calculateOutputSize(input, size)

        val output = createBitmap(outputWidth, outputHeight, input.config)
        output.applyCanvas {
            drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

            val matrix = Matrix()
            val multiplier = DecodeUtils.computeSizeMultiplier(
                srcWidth = input.width,
                srcHeight = input.height,
                dstWidth = outputWidth,
                dstHeight = outputHeight,
                scale = Scale.FILL
            ).toFloat()
            val dx = (outputWidth - multiplier * input.width) / 2
            val dy = (outputHeight - multiplier * input.height) / 2
            matrix.setTranslate(dx, dy)
            matrix.preScale(multiplier, multiplier)

            val shader = BitmapShader(input, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            shader.setLocalMatrix(matrix)
            paint.shader = shader

            val radii = floatArrayOf(
                topLeft, topLeft,
                topRight, topRight,
                bottomRight, bottomRight,
                bottomLeft, bottomLeft,
            )
            val halfBorderWidth = borderSize / 2f
            val borderPath = Path()
            borderPath.addRoundRect(
                halfBorderWidth, halfBorderWidth,
                width.toFloat() - halfBorderWidth,
                height.toFloat() - halfBorderWidth,
                radii, Path.Direction.CW
            )
            if (halfBorderWidth > 0) {
                radii.forEachIndexed { index, f -> radii[index] = f + halfBorderWidth }
            }

            val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
            val path = Path().apply { addRoundRect(rect, radii, Path.Direction.CW) }
            drawPath(path, paint)

            val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = borderColor
                style = Paint.Style.STROKE
                strokeWidth = borderSize.toFloat()
            }
            drawPath(borderPath, borderPaint)
        }

        return output
    }

    private fun calculateOutputSize(input: Bitmap, size: Size): Pair<Int, Int> {
        if (size.isOriginal) {
            return input.width to input.height
        }

        val (dstWidth, dstHeight) = size
        if (dstWidth is Dimension.Pixels && dstHeight is Dimension.Pixels) {
            return dstWidth.px to dstHeight.px
        }

        val multiplier = DecodeUtils.computeSizeMultiplier(
            srcWidth = input.width,
            srcHeight = input.height,
            dstWidth = size.width.pxOrElse { Int.MIN_VALUE },
            dstHeight = size.height.pxOrElse { Int.MIN_VALUE },
            scale = Scale.FILL
        )
        val outputWidth = (multiplier * input.width).roundToInt()
        val outputHeight = (multiplier * input.height).roundToInt()
        return outputWidth to outputHeight
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is RoundedCornersWithBorderTransformation &&
                topLeft == other.topLeft &&
                topRight == other.topRight &&
                bottomLeft == other.bottomLeft &&
                bottomRight == other.bottomRight &&
                borderSize == other.borderSize &&
                borderColor == other.borderColor
    }

    override fun hashCode(): Int {
        var result = topLeft.hashCode()
        result = 31 * result + topRight.hashCode()
        result = 31 * result + bottomLeft.hashCode()
        result = 31 * result + bottomRight.hashCode()
        result = 31 * result + borderSize.hashCode()
        result = 31 * result + borderColor.hashCode()
        return result
    }
}