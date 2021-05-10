package com.william.base_component.image

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
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
import com.william.base_component.R
import com.william.base_component.alias.OnGlideLoadFailed
import com.william.base_component.alias.OnGlideResourceReady
import com.william.base_component.annotations.TransformationType
import com.william.base_component.extension.dp
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation.CornerType


/**
 * 加载图片
 * @param context 上下文
 * @param url 图片资源
 * @param imageView  目标ImageView
 * @param transType 转换类型
 * @param radius 半径，默认2
 * @param borderWith 外框宽度，默认0f，如果为0，底层设置会取1px（仅对api >= 25 有效）
 * @param borderColor 外框颜色，默认#e6e6e6（仅对api >= 25 有效）
 * @param cornerType 圆角类型，默认CornerType.ALL
 * @param blurred 模糊度,[1,25]，默认10
 * @param scale 缩放度，默认1即不缩放
 * @param errorResId 错误图片id，默认base_icon_loading
 * @param skipMemoryCache 跳过缓存，默认false
 * @param width The width in pixels to use to load the resource.
 * @param height The height in pixels to use to load the resource.
 * @param onResourceReady 加载就绪的回调函数
 * @param onLoadFailed 加载失败的回调函数
 */
fun loadImage(
    context: Any?,
    url: Any?,
    imageView: ImageView?,
    @TransformationType transType: Int = DEFAULT_TRANSFORMATION,
    radius: Int = 2,
    borderWith: Float = 0f,
    @ColorRes borderColor: Int = R.color.color_e6e6e6,
    cornerType: CornerType = CornerType.ALL,
    @IntRange(from = 1, to = 25) blurred: Int = 10,
    scale: Int = 1,
    @DrawableRes errorResId: Int = R.drawable.base_icon_loading,
    skipMemoryCache: Boolean = false,
    width: Int? = null,
    height: Int? = width,
    onResourceReady: OnGlideResourceReady? = null,
    onLoadFailed: OnGlideLoadFailed? = null
) {
    val recourse: Any? = when (url) {
        is String -> url
        is Int -> url
        is ByteArray -> url
        else -> null
    }

    val requestBuilder: RequestBuilder<Drawable>? = getRequestManager(
        context
    )
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
                model: Any?,
                target: Target<Drawable?>?,
                isFirstResource: Boolean
            ): Boolean {
                onLoadFailed?.invoke(e, model, target, isFirstResource)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable?>?,
                dataSource: DataSource?,
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
    }
    if (width != null && height != null) {
        return options.override(width, height)
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
fun resumeRequests(context: Any?) {
    getRequestManager(context)?.resumeRequests()
}

/**
 * 中断加载
 */
fun pauseRequests(context: Any?) {
    getRequestManager(context)?.pauseRequests()
}

