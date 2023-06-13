package com.william.easykt.ui

import android.text.TextUtils
import coil.load
import com.william.base_component.BaseApp
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.base_component.extension.logD
import com.william.base_component.extension.logE
import com.william.base_component.extension.logI
import com.william.easykt.R
import com.william.easykt.databinding.ActivityLubanBinding
import dagger.hilt.android.AndroidEntryPoint
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Locale


/**
 *  author : WilliamYang
 *  date : 2023/6/13 13:11
 *  description : luban compress
 */
@AndroidEntryPoint
class LubanCompressActivity : BaseActivity() {

    override val viewBinding: ActivityLubanBinding by bindingView()

    override fun initData() {
        setTitleText(R.string.test_luban_compress)

        viewBinding.image1.load("file:///android_asset/fish.png") {
            crossfade(true)
            placeholder(R.drawable.ic_default)
            lifecycle(this@LubanCompressActivity)
        }
    }

    override fun initAction() {
        viewBinding.button.setOnClickListener {
            compress()
        }
    }

    private fun compress() {
        val list = assetsToFiles()

        Luban.with(BaseApp.instance)
            .load(list)
            .ignoreBy(40)
            .setFocusAlpha(false)
            .setTargetDir("$filesDir")
            .setRenameListener {
                "Rename: $it".logD()
                try {
                    val md = MessageDigest.getInstance("MD5")
                    md.update(it.toByteArray())
                    return@setRenameListener BigInteger(1, md.digest()).toString(32)
                } catch (e: NoSuchAlgorithmException) {
                    e.printStackTrace()
                }
                return@setRenameListener ""
            }
            .filter { path ->
                !(TextUtils.isEmpty(path) || path.lowercase(Locale.getDefault()).endsWith(".gif"))
            }
            .setCompressListener(object : OnCompressListener {
                override fun onStart() {
                    // 压缩开始前调用，可以在方法内启动 loading UI
                    "Compress image onStart...".logD()
                }

                override fun onSuccess(file: File) {
                    // 压缩成功后调用，返回压缩后的图片文件
                    "Compress image onSuccess, file path: ${file.absolutePath}".logI()
                    viewBinding.image2.load(file) {
                        crossfade(true)
                        placeholder(R.drawable.ic_default)
                        lifecycle(this@LubanCompressActivity)
                    }
                }

                override fun onError(e: Throwable) {
                    // 当压缩过程出现问题时调用
                    "Compress image onError: ${e.message}".logE()
                }
            }).launch()
    }

    private fun assetsToFiles(): List<File> {
        val files: MutableList<File> = ArrayList()
        try {
            val inputStream = resources.assets.open("fish.png")

            val file = File(filesDir, "fish_copy.png")
            val fos = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var len = inputStream.read(buffer)
            while (len > 0) {
                fos.write(buffer, 0, len)
                len = inputStream.read(buffer)
            }
            fos.close()
            inputStream.close()
            files.add(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return files
    }

}