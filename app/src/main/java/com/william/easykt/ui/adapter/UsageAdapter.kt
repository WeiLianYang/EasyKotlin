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
import com.william.base_component.utils.toText
import com.william.easykt.R
import com.william.easykt.data.UsageBean

/**
 * author：William
 * date：2021/8/27 22:43
 * description：usage adapter
 */
class UsageAdapter : BaseRvAdapter<UsageBean>() {

    override val layoutResourceId = R.layout.item_usage

    override fun onBindViewHolder(
        holder: BaseRvViewHolder,
        position: Int,
        bean: UsageBean?
    ) {
        val text = bean?.title ?: bean?.titleId?.toText()
        holder.setText(R.id.text, text)
    }
}