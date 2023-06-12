package com.william.easykt.ui.adapter

import com.william.base_component.adapter.BaseRvAdapter
import com.william.base_component.adapter.BaseRvViewHolder
import com.william.easykt.R
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 *  author : WilliamYang
 *  date : 2023/6/12 16:58
 *  description :
 */
@ActivityScoped
class PaneMenuAdapter @Inject constructor() : BaseRvAdapter<String>() {

    override val layoutResourceId = R.layout.item_main_entrance

    override fun onBindViewHolder(holder: BaseRvViewHolder, position: Int, bean: String?) {
        holder.setText(R.id.text, bean)
    }

}