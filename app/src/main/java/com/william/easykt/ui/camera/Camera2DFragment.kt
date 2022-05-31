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

package com.william.easykt.ui.camera

import android.annotation.SuppressLint
import android.widget.SeekBar
import com.william.base_component.extension.bindingView
import com.william.base_component.fragment.BaseFragment
import com.william.easykt.R
import com.william.easykt.databinding.FragmentCamera2dBinding


/**
 * @author William
 * @date 2022/5/29 22:32
 * Class Comment：Camera 2d transformations
 */
class Camera2DFragment : BaseFragment() {

    override val viewBinding by bindingView<FragmentCamera2dBinding>()

    override fun initAction() {
        viewBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_translate -> {
                    viewBinding.ivCamera2d.action = 0
                }
                R.id.rb_rotate -> {
                    viewBinding.ivCamera2d.action = 1
                }
            }
        }

        viewBinding.seekBarX.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewBinding.tvValueX.text = "X: ${viewBinding.seekBarX.progress}"
                update()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        viewBinding.seekBarY.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewBinding.tvValueY.text = "Y: ${viewBinding.seekBarY.progress}"
                update()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        viewBinding.seekBarZ.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewBinding.tvValueZ.text = "Z: ${viewBinding.seekBarZ.progress}"
                update()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun update() {
        viewBinding.ivCamera2d.setProgress(
            viewBinding.seekBarX.progress,
            viewBinding.seekBarY.progress,
            viewBinding.seekBarZ.progress
        )
    }

    override fun sendRequest() {
    }

}