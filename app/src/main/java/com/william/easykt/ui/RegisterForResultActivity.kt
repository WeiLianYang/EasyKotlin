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

import android.content.pm.PackageManager
import android.net.Uri
import com.william.base_component.BaseApp
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.toast
import com.william.easykt.databinding.ActivityRegisterForResultBinding
import com.william.easykt.utils.CropImageEntity
import com.william.easykt.utils.CropPhotoContract
import com.william.easykt.utils.SelectPhotoContract
import com.william.easykt.utils.TakePhotoContract
import kotlinx.coroutines.*

/**
 * author：William
 * date：2021/10/16 19:37
 * description：registerForActivityResult 用法详解及适配 Android 10、11 作用域存储
 * @see <a href="https://developer.android.com/training/basics/intents/result">获取 activity 的结果</a>
 * 官方预定义的 Contracts : [androidx.activity.result.contract.ActivityResultContracts]
 */
class RegisterForResultActivity : BaseActivity() {

    override val viewBinding: ActivityRegisterForResultBinding by bindingView()

    //    private val viewModel by viewModels<SampleViewModel>()
    private var needCrop = false

    override fun initView() {
        super.initView()

//        startActivityForResult()

        val cropPhoto = registerForActivityResult(CropPhotoContract()) { uri: Uri? ->
            if (uri != null) {
                viewBinding.ivImage.setImageURI(uri)
            }
        }
        val selectPhoto = registerForActivityResult(SelectPhotoContract()) { uri: Uri? ->
            if (uri != null) {
                if (needCrop) {
                    cropPhoto.launch(CropImageEntity(uri))
                } else {
                    viewBinding.ivImage.setImageURI(uri)
                }
            } else {
                "您没有选择任何图片".toast()
            }
        }
        val takePhoto =
            registerForActivityResult(TakePhotoContract()) { uri: Uri? ->
                if (uri != null) {
                    if (needCrop) {
                        cropPhoto.launch(CropImageEntity(uri))
                    } else {
                        viewBinding.ivImage.setImageURI(uri)
                    }
                }
            }

        viewBinding.btnSelectPhoto.setOnClickListener {
            // 从相册选择图片
            needCrop = false
            selectPhoto.launch(null)
        }
        viewBinding.btnSelectPhotoWithCrop.setOnClickListener {
            // 从相册选择并裁剪
            needCrop = true
            selectPhoto.launch(null)
        }

        viewBinding.btnTakePhoto.setOnClickListener {
            // 拍照
            if (!BaseApp.instance.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                "设备未安装相机应用，无法拍照".toast()
                return@setOnClickListener
            }
            needCrop = false
            takePhoto.launch(null)
        }
        viewBinding.btnTakePhotoWithCrop.setOnClickListener {
            // 拍照并剪裁图片
            if (!BaseApp.instance.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                "设备未安装相机应用，无法拍照".toast()
                return@setOnClickListener
            }
            needCrop = true
            takePhoto.launch(null)
        }
        viewBinding.btnClearPhoto.setOnClickListener {
            viewBinding.ivImage.setImageBitmap(null)
        }
    }

    override fun initData() {
        setTitleText("RegisterForActivityResult")
    }

    override fun initAction() {

    }

}