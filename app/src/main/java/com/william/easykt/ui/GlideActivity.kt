package com.william.easykt.ui

import androidx.activity.viewModels
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.base_component.extension.dp
import com.william.easykt.R
import com.william.easykt.databinding.ActivityGlideBinding
import com.william.easykt.ui.adapter.GlideImageAdapter
import com.william.easykt.viewmodel.GlideViewModel
import com.zyyoona7.itemdecoration.RecyclerViewDivider
import dagger.hilt.android.AndroidEntryPoint

/**
 *  author : WilliamYang
 *  date : 2022/8/22 16:24
 *  description :
 */
@AndroidEntryPoint
class GlideActivity : BaseActivity() {

    override val viewBinding: ActivityGlideBinding by bindingView()

    private val viewModel: GlideViewModel by viewModels()

    lateinit var glideAdapter: GlideImageAdapter

    override fun initData() {
        setTitleText(R.string.test_glide)
        viewBinding.recyclerView.apply {
            adapter = GlideImageAdapter(this@GlideActivity).also { glideAdapter = it }

            RecyclerViewDivider.grid()
                .dividerSize(15.dp).asSpace()
                .build().addTo(this)
        }

        viewModel.list.observe(this) {
            glideAdapter.setList(it)
        }
    }

}