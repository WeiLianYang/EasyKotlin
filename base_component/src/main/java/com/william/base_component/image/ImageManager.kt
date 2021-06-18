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