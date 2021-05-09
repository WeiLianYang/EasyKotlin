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