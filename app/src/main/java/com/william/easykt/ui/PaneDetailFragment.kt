package com.william.easykt.ui

import com.william.base_component.extension.bindingView
import com.william.base_component.fragment.BaseFragment
import com.william.easykt.databinding.FragmentPaneDetailBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *  author : WilliamYang
 *  date : 2023/6/12 17:11
 *  description :
 */
@AndroidEntryPoint
class PaneDetailFragment : BaseFragment() {

    override val viewBinding: FragmentPaneDetailBinding by bindingView()

    override fun initAction() {
        viewBinding.content.text = arguments?.getString("detail")
    }
}