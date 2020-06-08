package com.william.base_component.image

import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.william.base_component.alias.OnGlideLoadFailed
import com.william.base_component.alias.OnGlideResourceReady
import jp.wasabeef.glide.transformations.RoundedCornersTransformation.CornerType

open class BaseLoader : ILoaderInterface {

    override fun load(
        context: Any?,
        url: Any?,
        imageView: ImageView?,
        transType: Int,
        radius: Int,
        borderWith: Float,
        @ColorRes borderColor: Int,
        cornerType: CornerType,
        blurred: Int,
        scale: Int,
        @DrawableRes errorResId: Int,
        skipMemoryCache: Boolean,
        width: Int?,
        height: Int?,
        onResourceReady: OnGlideResourceReady?,
        onLoadFailed: OnGlideLoadFailed?
    ) {
    }

    override fun resumeRequests(context: Any?) {

    }

    override fun pauseRequests(context: Any?) {

    }
}