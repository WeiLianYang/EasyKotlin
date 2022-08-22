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

package com.william.base_component.image

import android.graphics.*
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.io.UnsupportedEncodingException
import java.security.MessageDigest

/**
 * @author William
 * @date 2020/5/8 12:26
 * Class Comment：
 */
class GlideRoundRectBorderTransform internal constructor(
    borderWidth: Float,
    borderColor: Int,
    radius: Int
) : BitmapTransformation() {
    /**
     * 外边框的画笔
     */
    private val mBorderPaint: Paint?

    /**
     * 外边框的宽度
     */
    private val mBorderWidth: Float

    /**
     * 外边框的圆角大小
     */
    private val mRadius: Float
    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return circleCrop(pool, toTransform)!!
    }

    private fun circleCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
        if (source == null) {
            return null
        }
        // 获取原图宽高
        val width = source.width
        val height = source.height
        // 从位图池中取出可以复用的位图
        val poolBitmap = pool[width, height, Bitmap.Config.ARGB_8888]
        poolBitmap.setHasAlpha(true)
        val canvas = Canvas(poolBitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        // 给画笔设置着色器
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        // 一半的边框宽度
        val halfBorderSize = mBorderWidth / 2

        // 剪切内部资源图片的圆角，这里没有以border的宽度为起始点绘制，因为在实际展示中可能会出现有缝隙的情况
        val innerRectF =
            RectF(halfBorderSize, halfBorderSize, width - halfBorderSize, height - halfBorderSize)
        canvas.drawRoundRect(innerRectF, mRadius, mRadius, paint)
        if (mBorderPaint != null) {
            // 绘制外部的圆角边框。需要注意画笔是以边框宽度的一半为起始点绘制，因为画笔的中心是给定绘制边框宽度的一半处，否则会出现边框被裁掉
            val borderRectF = RectF(
                halfBorderSize,
                halfBorderSize,
                width - halfBorderSize,
                height - halfBorderSize
            )
            canvas.drawRoundRect(borderRectF, mRadius, mRadius, mBorderPaint)
        }
        return poolBitmap
    }

    override fun equals(other: Any?): Boolean {
        return other is GlideRoundRectBorderTransform
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        try {
            messageDigest.update(ID.toByteArray(charset(Key.STRING_CHARSET_NAME)))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val ID = "com.etechs.eyee.utils.image.GlideRoundRectBorderTransform"
        private const val BORDER_WIDTH_MINIMUM = 1f
    }

    init {
        mRadius = radius.toFloat()
        mBorderWidth = BORDER_WIDTH_MINIMUM.coerceAtLeast(borderWidth)
        mBorderPaint = Paint()
        mBorderPaint.isDither = true
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = borderColor
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.strokeWidth = mBorderWidth
    }
}