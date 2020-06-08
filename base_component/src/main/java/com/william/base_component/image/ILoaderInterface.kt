package com.william.base_component.image

import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import com.william.base_component.R
import com.william.base_component.alias.OnGlideLoadFailed
import com.william.base_component.alias.OnGlideResourceReady
import com.william.base_component.annotations.TransformationType
import jp.wasabeef.glide.transformations.RoundedCornersTransformation.CornerType

const val TYPE_NORMAL = 0// 正常模式
const val TYPE_CIRCLE = 1// 加载圆形图片
const val TYPE_CIRCLE_BORDER = 2// 加载圆形图片带外框
const val TYPE_ROUND_RECT = 3// 加载圆角矩形图片带外框
const val TYPE_RADIUS = 4// 加载带圆角图片
const val TYPE_RADIUS_NOT_CROP = 5// 加载带圆角图片，不附加centerCrop效果
const val TYPE_BLURRED = 6// 高斯模糊
const val TYPE_BLURRED_RADIUS = 7// 高斯模糊带圆角

const val DEFAULT_TRANSFORMATION = TYPE_NORMAL// 默认转换

interface ILoaderInterface {

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
    fun load(
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
    }

    /**
     * @param context 上下文
     * 恢复请求
     */
    fun resumeRequests(context: Any?)

    /**
     * @param context 上下文
     * 暂停请求
     */
    fun pauseRequests(context: Any?)
}