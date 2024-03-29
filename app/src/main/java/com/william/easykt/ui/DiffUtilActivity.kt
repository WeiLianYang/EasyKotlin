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

package com.william.easykt.ui

import androidx.activity.viewModels
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.base_component.extension.toPx
import com.william.easykt.R
import com.william.easykt.databinding.ActivityDiffUtilBinding
import com.william.easykt.ui.adapter.DiffUtilAdapter
import com.william.easykt.viewmodel.DiffUtilViewModel
import com.zyyoona7.itemdecoration.RecyclerViewDivider


/**
 * @author William
 * @date 2021/9/13 21:36
 * Class Comment：
 */
class DiffUtilActivity : BaseActivity() {

    override val viewBinding by bindingView<ActivityDiffUtilBinding>()

    private val viewModel by viewModels<DiffUtilViewModel>()

    private lateinit var diffAdapter: DiffUtilAdapter

    override fun initAction() {
        viewModel.listData.observe(this) {
            diffAdapter.submitList(it)
        }

        viewBinding.btnChange1.setOnClickListener {
            diffAdapter.submitList(viewModel.getList(1))
        }
        viewBinding.btnChange2.setOnClickListener {
            diffAdapter.submitList(viewModel.getList(2))
        }
    }

    override fun initData() {
        setTitleText("DiffUtilActivity")

        viewBinding.recyclerView.apply {
            adapter = DiffUtilAdapter().also {
                diffAdapter = it
            }
            RecyclerViewDivider.linear()
                .asSpace().dividerSize(R.dimen.dp_10.toPx())
                .build().addTo(this)
        }
    }


}