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

package com.william.easykt.ui.adapter


import com.william.base_component.adapter.BaseRvAdapter
import com.william.base_component.adapter.BaseRvViewHolder
import com.william.easykt.R

/**
 * author：William
 * date：4/23/21 10:39 PM
 * description：可滚动图片recyclerview的adapter
 */
class ScrollImageAdapter : BaseRvAdapter<Any>() {

    override val layoutResourceId: Int
        get() = R.layout.item_scroll_image

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun onBindViewHolder(holder: BaseRvViewHolder, position: Int, bean: Any?) {
        val imageRes = R.drawable.scroll_image
        holder.setImageResource(R.id.ivScrollImage, imageRes)
    }
}