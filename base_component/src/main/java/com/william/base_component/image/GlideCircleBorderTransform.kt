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

import android.content.res.Resources
import android.graphics.*
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.io.UnsupportedEncodingException
import java.security.MessageDigest

class GlideCircleBorderTransform internal constructor(borderWidth: Float, borderColor: Int) :
    BitmapTransformation() {
    private var mBorderPaint: Paint
    private var mBorderWidth = 0f

    init {
        val density =
            Resources.getSystem().displayMetrics.density
        mBorderWidth = BORDER_WIDTH_MINIMUM.coerceAtLeast(density * borderWidth)
        mBorderPaint = Paint()
        mBorderPaint.isDither = true
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = borderColor
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.strokeWidth = mBorderWidth
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return circleCrop(pool, toTransform)!!
    }

    private fun circleCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
        if (source == null) return null
        val size = (source.width.coerceAtMost(source.height) - mBorderWidth / 2).toInt()
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        val squared = Bitmap.createBitmap(source, x, y, size, size)
        var result: Bitmap? = pool[size, size, Bitmap.Config.ARGB_8888]
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(result!!)
        val paint = Paint()
        paint.shader = BitmapShader(
            squared,
            Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )
        paint.isAntiAlias = true
        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)
        val borderRadius = r - mBorderWidth / 2
        canvas.drawCircle(r, r, borderRadius, mBorderPaint)

        return result
    }

    override fun equals(other: Any?): Boolean {
        return other is GlideCircleBorderTransform
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
        private const val ID = "com.etechs.eyee.utils.image.GlideCircleBorderTransform"

        /**
         * 默认1px
         */
        private const val BORDER_WIDTH_MINIMUM = 1f
    }
}