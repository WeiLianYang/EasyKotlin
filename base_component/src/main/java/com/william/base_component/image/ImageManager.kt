package com.william.base_component.image

import android.widget.ImageView
import com.william.base_component.alias.OnGlideLoadFailed
import com.william.base_component.alias.OnGlideResourceReady
import jp.wasabeef.glide.transformations.RoundedCornersTransformation.CornerType

object ImageManager : ILoaderInterface {
    private val baseLoader: BaseLoader

    init {
        baseLoader = GlideLoader()
    }

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
        baseLoader.load(
            context, url, imageView, transType,
            radius, borderWith, borderColor, cornerType,
            blurred, scale,
            errorResId, skipMemoryCache,
            width, height,
            onResourceReady, onLoadFailed
        )
    }

    override fun resumeRequests(context: Any?) {
        baseLoader.resumeRequests(context)
    }

    override fun pauseRequests(context: Any?) {
        baseLoader.pauseRequests(context)
    }
}