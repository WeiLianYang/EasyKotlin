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

package com.william.easykt

import android.content.Intent
import android.os.Build
import android.os.storage.StorageManager
import androidx.core.content.getSystemService
import com.flurry.android.FlurryAgent
import com.william.base_component.activity.BaseActivity
import com.william.base_component.utils.logD
import com.william.base_component.utils.openActivity
import com.william.base_component.utils.openBrowser
import com.william.easykt.databinding.ActivityMainBinding
import com.william.easykt.test.TestActivity
import com.william.easykt.ui.*
import java.util.*


/**
 * @author William
 * @date 4/10/21 7:23 PM
 * Class Comment：主入口
 */
class MainActivity : BaseActivity() {
    override val mViewBinding: ActivityMainBinding by bindingView()

    override fun initAction() {

        mViewBinding.apply {
            tvButton1.setOnClickListener {
                openActivity<TestActivity>(mActivity) {
                    putExtra("name", "Stark")
                }
            }

            tvButton2.setOnClickListener {
                openActivity<SwipeCardActivity>(mActivity)
            }

            tvButton3.setOnClickListener {
                openActivity<WaveAnimationActivity>(mActivity)
            }

            tvButton4.setOnClickListener {
                openActivity<PagerCardActivity>(mActivity)
            }

            tvButton5.setOnClickListener {
                openActivity<AutoScrollActivity>(mActivity)
            }

            tvButton6.setOnClickListener {
                openActivity<PermissionActivity>(mActivity)
            }

            tvButton7.setOnClickListener {
                /*
                openBrowser(
                    mActivity,
                    "https://www.baidu.com",
                    "com.tencent.mtt",
                    "com.tencent.mtt.MainActivity"
                )
                */
                openBrowser(this@MainActivity, "https://www.baidu.com")
            }

            tvButton8.setOnClickListener {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                    // App needs 10 MB within internal storage.
                    val numBytesNeededForMyApp = 1024 * 1024 * 10L
                    val storageManager = applicationContext.getSystemService<StorageManager>()!!
                    val appSpecificInternalDirUuid: UUID = storageManager.getUuidForPath(filesDir)
                    val availableBytes =
                        storageManager.getAllocatableBytes(appSpecificInternalDirUuid)
                    val kb = availableBytes / 1024
                    val mb = kb / 1024
                    val gb = mb / 1024
                    "available space : $availableBytes byte , $kb kb ,  $mb MB , $gb GB".logD()
                    if (availableBytes >= numBytesNeededForMyApp) {
                        storageManager.allocateBytes(
                            appSpecificInternalDirUuid,
                            numBytesNeededForMyApp
                        )
                    } else {
                        val storageIntent = Intent(StorageManager.ACTION_MANAGE_STORAGE)
                        startActivity(storageIntent)
                    }
                }
            }

            tvButton9.setOnClickListener {
                // param keys and values have to be of String type
                val map = hashMapOf("user" to "Jack", "age" to "18")
                // up to 10 params can be logged with each event
                FlurryAgent.logEvent("userEvent", map)
            }

            tvButton10.setOnClickListener {
//                throw IndexOutOfBoundsException()
                kotlin.runCatching {
                    throw NullPointerException()
                }.onFailure {
                    FlurryAgent.onError("errorId", "message", it)
                }
            }
            
            tvButton11.setOnClickListener {
                openActivity<NestedScrollingActivity>(mActivity)
            }

        }
    }

    override fun initData() {
        setTitleText("EasyKotlin")
    }

}
