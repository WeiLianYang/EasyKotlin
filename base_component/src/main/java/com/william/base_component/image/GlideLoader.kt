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

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.william.base_component.BaseApp
import com.william.base_component.alias.OnGlideLoadFailed
import com.william.base_component.alias.OnGlideResourceReady
import com.william.base_component.extension.dp
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation.CornerType

class GlideLoader : BaseLoader() {

    override fun load(
        context: Any?,
        url: Any?,
        imageView: ImageView?,
        transType: Int,
        radius: Int,
        borderWith: Float,
        borderColor: Int,
        cornerType: CornerType,
        blurred: Int,
        scale: Int,
        errorResId: Int,
        skipMemoryCache: Boolean,
        width: Int?,
        height: Int?,
        onResourceReady: OnGlideResourceReady?,
        onLoadFailed: OnGlideLoadFailed?
    ) {
        val recourse: Any = when (url) {
            is String -> url
            is Int -> url
            is ByteArray -> url
            else -> return
        }

        val requestBuilder: RequestBuilder<Drawable>? = getRequestManager(context)
            ?.load(recourse)
            ?.apply(
                createOptions(
                    transType, radius, borderWith, borderColor, cornerType,
                    blurred, scale, errorResId, skipMemoryCache, width, height
                )
            )
            ?.transition(DrawableTransitionOptions.withCrossFade())
            ?.addListener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    onLoadFailed?.invoke(e, model, target, isFirstResource)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    onResourceReady?.invoke(
                        resource,
                        model,
                        target,
                        dataSource,
                        isFirstResource
                    )
                    return false
                }
            })
        // 可以只做图片加载监听，不一定非要加载到某个ImageView中
        imageView?.let {
            requestBuilder?.into(it)
        }
    }

    /**
     * 获取图片请求管理对象
     *
     * @param param context资源对象
     * @return RequestManager
     */
    private fun getRequestManager(param: Any?): RequestManager? {
        param.let {
            return when (it) {
                is Activity -> {
                    Glide.with(it)
                }
                is Fragment -> {
                    Glide.with(it)
                }
                is View -> {
                    Glide.with(it)
                }
                is Context -> {
                    Glide.with(it)
                }
                else -> null
            }
        }
    }

    /**
     * 创建RequestOptions
     */
    private fun createOptions(
        transType: Int,
        radius: Int,
        borderWith: Float,
        @ColorRes borderColor: Int,
        cornerType: CornerType,
        blurred: Int,
        scale: Int,
        errorResId: Int,
        skipMemoryCache: Boolean,
        width: Int?,
        height: Int?
    ): BaseRequestOptions<*> {
        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .skipMemoryCache(skipMemoryCache)
            .error(errorResId)

        if (transType != TYPE_NORMAL) {
            val transformation = getMultiTransformation(
                transType, radius, borderWith,
                borderColor, cornerType, blurred, scale
            )
            transformation?.let {
                options.transform(it)
            }

            if (width != null && height != null) {
                return options.override(width, height)
            }
        }
        return options
    }

    /**
     * 创建MultiTransformation<Bitmap>，用于各种自定义效果
     */
    private fun getMultiTransformation(
        transformationType: Int,
        radius: Int,
        borderWith: Float,
        @ColorRes borderColor: Int,
        cornerType: CornerType,
        blurred: Int,
        scale: Int
    ): MultiTransformation<Bitmap>? {
        val list = mutableListOf<Transformation<Bitmap>>()

        when (transformationType) {
            TYPE_CIRCLE -> list.add(CircleCrop())
            TYPE_CIRCLE_BORDER -> {
                list.apply {
                    add(CircleCrop())
                    add(
                        GlideCircleBorderTransform(
                            borderWith,
                            BaseApp.instance.getColor(borderColor)
                        )
                    )
                }
            }
            TYPE_ROUND_RECT -> {
                list.apply {
                    add(CenterCrop())
                    add(
                        GlideRoundRectBorderTransform(
                            borderWith,
                            BaseApp.instance.getColor(borderColor),
                            radius
                        )
                    )
                }
            }
            TYPE_RADIUS -> {
                list.apply {
                    add(CenterCrop())
                    add(RoundedCornersTransformation(radius.dp, 0, cornerType))
                }
            }
            TYPE_RADIUS_NOT_CROP -> {
                list.add(RoundedCornersTransformation(radius.dp, 0, cornerType))
            }
            TYPE_BLURRED -> list.add(BlurTransformation(blurred, scale))
            TYPE_BLURRED_RADIUS -> {
                // TODO: William 2020/5/24 17:11 这种模式下四周会留下小黑点，原因待查
                list.apply {
                    add(CenterCrop())
                    add(RoundedCornersTransformation(radius.dp, 0, cornerType))
                    add(BlurTransformation(blurred, scale))
                }
            }
        }
        return if (list.size > 0)
            MultiTransformation(list)
        else {
            null
        }
    }

    /**
     * 恢复加载
     */
    override fun resumeRequests(context: Any?) {
        getRequestManager(context)?.resumeRequests()
    }

    /**
     * 中断加载
     */
    override fun pauseRequests(context: Any?) {
        getRequestManager(context)?.pauseRequests()
    }
}
