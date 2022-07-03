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

package com.william.easykt.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.william.easykt.R

/**
 * author：William
 * date：2022/7/3 15:23
 * description：Volume Layout
 */
class VolumeLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(
    context,
    attrs,
    defStyleAttr
) {
    private var mVolumeView: VolumeView? = null

    init {
        init(context)
    }

    private fun init(context: Context) {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.volume_layout, this)
        mVolumeView = view.findViewById(R.id.volume)
    }

    fun volumeUp() {
        mVolumeView?.volumeUp()
    }

    fun volumeDown() {
        mVolumeView?.volumeDown()
    }
}