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

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.storage.StorageManager
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.flurry.android.FlurryAgent
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.base_component.utils.*
import com.william.easykt.databinding.ActivityMainBinding
import com.william.easykt.test.TestVmActivity
import com.william.easykt.ui.*
import com.william.easykt.ui.adapter.MainAdapter
import com.william.easykt.utils.getBuildInfo
import com.william.easykt.viewmodel.MainViewModel
import com.zyyoona7.itemdecoration.RecyclerViewDivider
import java.util.*


/**
 * @author William
 * @date 4/10/21 7:23 PM
 * Class Comment：主入口
 */
class MainActivity : BaseActivity() {
    override val viewBinding: ActivityMainBinding by bindingView()

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var mAdapter: MainAdapter

    override fun initAction() {
        viewBinding.recyclerView.apply {
            adapter = MainAdapter().also {
                mAdapter = it
            }
            RecyclerViewDivider.staggeredGrid()
                .includeStartEdge()
                .includeEdge().spacingSize(R.dimen.dp_10.toPx())
                .build().addTo(this)
        }

        viewModel.dataList.observe(this) {
            mAdapter.setList(it)
        }

        mAdapter.setOnItemClickListener { _, position, _ ->

            when (position) {
                0 -> openActivity<TestVmActivity>(mActivity)
                1 -> openActivity<SwipeCardActivity>(mActivity)
                2 -> openActivity<WaveAnimationActivity>(mActivity)
                3 -> openActivity<PagerCardActivity>(mActivity)
                4 -> openActivity<AutoScrollActivity>(mActivity)
                5 -> openActivity<PermissionActivity>(mActivity)
                6 -> openBrowser(this, "https://www.baidu.com")
                7 -> {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                        // App needs 10 MB within internal storage.
                        val numBytesNeededForMyApp = 1024 * 1024 * 10L
                        val storageManager = applicationContext.getSystemService<StorageManager>()!!
                        val appSpecificInternalDirUuid: UUID =
                            storageManager.getUuidForPath(filesDir)
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
                8 -> {
                    // param keys and values have to be of String type
                    val map = hashMapOf("user" to "Jack", "age" to "18")
                    // up to 10 params can be logged with each event
                    FlurryAgent.logEvent("userEvent", map)
                }
                9 -> {
                    kotlin.runCatching {
                        throw NullPointerException()
                    }.onFailure {
                        FlurryAgent.onError("errorId", "message", it)
                    }
                }
                10 -> openActivity<NestedScrollingActivity>(mActivity)
                11 -> openActivity<FlowSampleActivity>(mActivity)
                12 -> SampleSheetDialog.show(mActivity.supportFragmentManager)
                13 -> openActivity<ChannelSampleActivity>(mActivity)
                14 -> {
                    showSnackbar(viewBinding.root, "Hello Snackbar", "execute") {
                        showSnackbar(viewBinding.root, R.string.test_success)
                    }
                }
                15 -> openActivity<DiffUtilActivity>(mActivity)
                16 -> openActivity<CoroutineSampleActivity>(mActivity)
                17 -> openActivity<RegisterForResultActivity>(mActivity)
                18 -> openActivity<FileActivity>(mActivity)
                19 -> sendNotification()
                20 -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    queryIntent(this, intent)
                    getInstalledPackages(this)
                    getPackageInfo(this, "com.android.chrome")
                }
                21 -> openActivity<ComposeActivity>(mActivity)
                22 -> openActivity<ComposeDemoActivity>(mActivity)
                23 -> openActivity<ComposeLayoutActivity>(mActivity)
                24 -> openActivity<WindowInsetsActivity>(mActivity)
                25 -> openActivity<AesEncryptActivity>(mActivity)
                else -> {
                }
            }
        }
    }

    override fun initData() {
        setTitleText("EasyKotlin")

        getBuildInfo()

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        "onNewIntent() is called".logD()
        setIntent(intent)

        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (null != intent) {
            val map = hashMapOf<String, String>()
            // 获取data里的值
            val bundle = intent.extras
            bundle?.keySet()?.forEach { key ->
                val content = bundle.get(key)
                "receive data, key = $key, content = $content".logI()
                map[key] = content.toString()
            }
            "map===> $map".logI()
        } else {
            "intent = null".logW()
        }
    }

    private var notificationId = 0

    private fun sendNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("key1", "value1")
            putExtra("key2", 2222)
            putExtra("key3", true)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val channelId = getString(R.string.default_notification_channel_id)
        val channelName = getString(R.string.default_notification_channel_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Notification title")
            .setContentText("Notification content")
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(
                    channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
        notificationId++
        manager.notify(notificationId, builder.build())
    }

}
