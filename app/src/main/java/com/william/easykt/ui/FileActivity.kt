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

import com.google.gson.Gson
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.base_component.extension.logD
import com.william.base_component.extension.logE
import com.william.base_component.extension.logI
import com.william.base_component.extension.logW
import com.william.easykt.data.Banner
import com.william.easykt.databinding.ActivityFileBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.concurrent.thread


/**
 * @author WilliamYang
 * @date 2021/2/19 13:22
 * Class Comment：权限
 */
class FileActivity : BaseActivity() {

    override val viewBinding: ActivityFileBinding by bindingView()

    override fun initAction() {
    }

    private val fileName = "test_file.txt"

    override fun initData() {
        setTitleText(javaClass.simpleName)

        viewBinding.btnWrite1.setOnClickListener {
            writeFile1()
        }

        viewBinding.btnRead1.setOnClickListener {
            readFile1()
        }

        viewBinding.btnWrite2.setOnClickListener {
            writeFile2()
        }

        viewBinding.btnRead2.setOnClickListener {
            readFile2()
        }

        viewBinding.btnDelete.setOnClickListener {
            deleteFile()
        }
    }

    private fun writeFile1() {
        thread {
            val banner = Banner(
                desc = "test desc",
                id = 1,
                imagePath = "test path",
                isVisible = 2,
                order = 3,
                title = "test title",
                type = 4,
                url = "test url"
            )
            val json = Gson().toJson(banner)
            kotlin.runCatching {
                val outputStream = openFileOutput(fileName, MODE_PRIVATE)
                outputStream.use {
                    it.write(json.toByteArray())
                }
            }.onSuccess {
                "write file success".logI()
            }.onFailure {
                "write file failed: $it".logE()
            }
        }
    }

    private fun readFile1() {
        thread {
            kotlin.runCatching {
                var banner: Banner? = null
                openFileInput(fileName).bufferedReader().useLines { lines ->
                    val result = lines.fold("") { some, text ->
                        "$some$text"
                    }
                    result.logD()
                    banner = Gson().fromJson(result, Banner::class.java)
                }
                banner
            }.onSuccess {
                "read file success: $it".logI()
            }.onFailure {
                "read file failed: $it".logE()
            }
        }
    }

    private fun writeFile2() {
        thread {
            val banner = Banner(
                desc = "test desc",
                id = 1,
                imagePath = "test path",
                isVisible = 2,
                order = 3,
                title = "test title",
                type = 4,
                url = "test url"
            )
            val json = Gson().toJson(banner)
            kotlin.runCatching {
                val file = File(filesDir, fileName)
                val fos = FileOutputStream(file)
                fos.use {
                    it.write(json.toByteArray())
                }
            }.onSuccess {
                "write file success".logI()
            }.onFailure {
                "write file failed: $it".logE()
            }
        }
    }

    private fun readFile2() {
        thread {
            var banner: Banner? = null
            kotlin.runCatching {
                val file = File(filesDir, fileName)
                if (file.exists()) {
                    // 文件存在
                    FileInputStream(file).bufferedReader().useLines { lines ->
                        val result = lines.fold("") { some, text ->
                            "$some$text"
                        }
                        result.logD()
                        banner = Gson().fromJson(result, Banner::class.java)
                    }
                } else {
                    "file $fileName not exist".logW()
                }
                banner
            }.onFailure {
                "read file failed: $it".logE()
            }
            "read file: $banner".logI()
        }
    }

    private fun deleteFile() {
        val result = deleteFile(fileName)
        "delete file result: $result".logD()
    }

}