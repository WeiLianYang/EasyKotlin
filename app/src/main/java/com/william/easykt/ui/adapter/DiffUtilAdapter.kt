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

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.william.easykt.data.DiffUtilBean
import com.william.easykt.databinding.ItemDiffBinding

/**
 * @author William
 * @date 2021/9/13 21:39
 * Class Comment：
 */
class DiffUtilAdapter : ListAdapter<DiffUtilBean, DiffUtilAdapter.UsageViewHolder>(DiffCallback()) {

    private val dataList = mutableListOf<DiffUtilBean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsageViewHolder {
        val binding = ItemDiffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsageViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UsageViewHolder, position: Int) {
        val itemData = dataList[position]
        holder.binding.tvId.text = "id: ${itemData.id}"
        holder.binding.tvTitle.text = "title: ${itemData.title}"
        holder.binding.tvContent.text = "content: ${itemData.content}"
    }

    override fun onBindViewHolder(
        holder: UsageViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }

    class UsageViewHolder(val binding: ItemDiffBinding) : RecyclerView.ViewHolder(binding.root)

    override fun submitList(list: List<DiffUtilBean>?) {
        if (list != null) {
            dataList.clear()
            dataList.addAll(list)
        }
        super.submitList(list)
    }

}

class DiffCallback : DiffUtil.ItemCallback<DiffUtilBean>() {
    override fun areItemsTheSame(oldItem: DiffUtilBean, newItem: DiffUtilBean): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DiffUtilBean, newItem: DiffUtilBean): Boolean {
        return oldItem.title == newItem.title && oldItem.content == newItem.content
    }
}

