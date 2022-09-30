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

import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.base_component.extension.toast
import com.william.easykt.R
import com.william.easykt.databinding.ActivityMediaPickerBinding
import com.william.easykt.utils.MediaContract
import com.william.easykt.utils.MediaPickerParams

/**
 * author：William
 * date：2021/10/16 19:37
 * description：媒体选择器 示例
 * @see <a href="https://developer.android.com/about/versions/13/features/photopicker">照片选择器</a>
 * 官方预定义的 Contracts : [androidx.activity.result.contract.ActivityResultContracts]
 */
class MediaPickerActivity : BaseActivity() {

    override val viewBinding: ActivityMediaPickerBinding by bindingView()

    private var mediaLauncher: ActivityResultLauncher<MediaPickerParams>? = null

    override fun initView() {
        super.initView()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mediaLauncher = registerForActivityResult(MediaContract()) { list: List<Uri> ->
                if (list.isNotEmpty()) {
                    viewBinding.ivImage.setImageURI(list.first())
                } else {
                    "您没有选择任何图片".toast()
                }
            }
        }
    }

    override fun initData() {
        setTitleText(R.string.test_media_picker)
    }

    override fun initAction() {
        viewBinding.btnSelectImage.setOnClickListener {
            // 只选择照片
            val params = MediaPickerParams(MediaPickerParams.SELECT_IMAGE)
            mediaLauncher?.launch(params)
        }

        viewBinding.btnSelectVideo.setOnClickListener {
            // 只选择视频
            val params = MediaPickerParams(MediaPickerParams.SELECT_VIDEO)
            mediaLauncher?.launch(params)
        }

        viewBinding.btnClearPhoto.setOnClickListener {
            viewBinding.ivImage.setImageBitmap(null)
        }
    }

}