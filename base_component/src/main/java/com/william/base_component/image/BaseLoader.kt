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