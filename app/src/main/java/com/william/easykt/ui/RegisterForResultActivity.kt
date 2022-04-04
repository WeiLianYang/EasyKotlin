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

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.william.base_component.BaseApp
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.base_component.extension.logE
import com.william.base_component.extension.logV
import com.william.base_component.extension.toast
import com.william.easykt.databinding.ActivityRegisterForResultBinding
import com.william.easykt.utils.CropParams
import com.william.easykt.utils.CropPhotoContract
import com.william.easykt.utils.SelectPhotoContract
import com.william.easykt.utils.TakePhotoContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * author：William
 * date：2021/10/16 19:37
 * description：registerForActivityResult 用法详解及适配 Android 10、11 作用域存储
 * @see <a href="https://developer.android.com/training/basics/intents/result">获取 activity 的结果</a>
 * 官方预定义的 Contracts : [androidx.activity.result.contract.ActivityResultContracts]
 */
class RegisterForResultActivity : BaseActivity() {

    override val viewBinding: ActivityRegisterForResultBinding by bindingView()

    private var needCrop = false

    override fun initView() {
        super.initView()

        val cropPhoto = registerForActivityResult(CropPhotoContract()) { uri: Uri? ->
            if (uri != null) {
                viewBinding.ivImage.setImageURI(uri)
            }
        }
        val selectPhoto = registerForActivityResult(SelectPhotoContract()) { uri: Uri? ->
            if (uri != null) {
                if (needCrop) {
                    lifecycleScope.launch {
                        val googlePrefix =
                            "content://com.google.android.apps.photos.contentprovider"
                        val newUri = if (uri.toString().startsWith(googlePrefix, true)) {
                            // 处理谷歌相册返回的图片
                            saveImageToCache(this@RegisterForResultActivity, uri)
                        } else uri
                        // 剪裁图片
                        kotlin.runCatching {
                            cropPhoto.launch(CropParams(newUri))
                        }.onFailure {
                            "crop failed: $it".logE()
                        }
                    }
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
                        cropPhoto.launch(CropParams(uri))
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
            // 从相册选择并剪裁
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

    /**
     * 将谷歌相册图片保存到外置存储目录，然后返回 uri
     */
    private suspend fun saveImageToCache(context: Context, uri: Uri): Uri {
        val imageName = "${System.currentTimeMillis()}.jpg"
        val parent = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            context.externalCacheDir?.absolutePath
        } else {
            context.cacheDir?.absolutePath
        }
        val path = parent + File.separator + imageName

        withContext(Dispatchers.IO) {
            copyInputStream(context, uri, path)
        }

        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context, "${context.packageName}.fileprovider",
                File(parent, imageName)
            )
        } else {
            Uri.fromFile(File(path))
        }
        "uri: $result".logV()
        return result
    }

    /**
     * 字节流读写复制文件
     * @param context 上下文
     * @param uri 图片uri
     * @param outputPath 输出地址
     */
    private fun copyInputStream(context: Context, uri: Uri, outputPath: String) {
        "copy file begin...".logV()
        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(uri)
            outputStream = FileOutputStream(outputPath)
            val bytes = ByteArray(1024)
            var num: Int
            while (inputStream?.read(bytes).also { num = it ?: -1 } != -1) {
                outputStream.write(bytes, 0, num)
                outputStream.flush()
            }
        } catch (e: Exception) {
            "exception: $e".logE()
        } finally {
            try {
                outputStream?.close()
                inputStream?.close()
                "copy file end...".logV()
            } catch (e: IOException) {
                "exception: $e".logE()
            }
        }
    }

}