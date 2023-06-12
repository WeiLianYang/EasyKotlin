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

import android.animation.ObjectAnimator
import android.app.AppOpsManager
import android.app.AsyncNotedAppOp
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.SyncNotedAppOp
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.storage.StorageManager
import android.provider.Settings
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.activityManager
import com.william.base_component.extension.bindingView
import com.william.base_component.extension.logD
import com.william.base_component.extension.logE
import com.william.base_component.extension.logI
import com.william.base_component.extension.logW
import com.william.base_component.extension.toPx
import com.william.base_component.extension.toast
import com.william.base_component.utils.getInstalledPackages
import com.william.base_component.utils.getPackageInfo
import com.william.base_component.utils.openActivity
import com.william.base_component.utils.openBrowser
import com.william.base_component.utils.queryIntent
import com.william.base_component.utils.showSnackbar
import com.william.easykt.databinding.ActivityMainBinding
import com.william.easykt.test.TestVmActivity
import com.william.easykt.ui.AesEncryptActivity
import com.william.easykt.ui.AlarmManagerActivity
import com.william.easykt.ui.AutoScrollActivity
import com.william.easykt.ui.BubbleActivity
import com.william.easykt.ui.CameraxActivity
import com.william.easykt.ui.ChannelSampleActivity
import com.william.easykt.ui.ClockViewActivity
import com.william.easykt.ui.CoilActivity
import com.william.easykt.ui.ComposeActivity
import com.william.easykt.ui.ComposeDemoActivity
import com.william.easykt.ui.ComposeLayoutActivity
import com.william.easykt.ui.CoroutineSampleActivity
import com.william.easykt.ui.DataStoreActivity
import com.william.easykt.ui.DiffUtilActivity
import com.william.easykt.ui.DrawerActivity
import com.william.easykt.ui.FileActivity
import com.william.easykt.ui.FlowSampleActivity
import com.william.easykt.ui.GlideActivity
import com.william.easykt.ui.MediaPickerActivity
import com.william.easykt.ui.MotionLayoutActivity
import com.william.easykt.ui.NestedScrollingActivity
import com.william.easykt.ui.PagerCardActivity
import com.william.easykt.ui.PermissionActivity
import com.william.easykt.ui.ProfilerActivity
import com.william.easykt.ui.RegisterForResultActivity
import com.william.easykt.ui.RomInfoActivity
import com.william.easykt.ui.RoundCornerLayoutActivity
import com.william.easykt.ui.RoundImageActivity
import com.william.easykt.ui.SampleSheetDialog
import com.william.easykt.ui.SectorViewActivity
import com.william.easykt.ui.SideSlipActivity
import com.william.easykt.ui.SlidingPaneActivity
import com.william.easykt.ui.SwipeCardActivity
import com.william.easykt.ui.TouchImageActivity
import com.william.easykt.ui.WaveAnimationActivity
import com.william.easykt.ui.WebActivity
import com.william.easykt.ui.WindowInsetsActivity
import com.william.easykt.ui.adapter.MainAdapter
import com.william.easykt.ui.camera.CameraActivity
import com.william.easykt.utils.createBubble
import com.william.easykt.viewmodel.MainViewModel
import com.zyyoona7.itemdecoration.RecyclerViewDivider
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import javax.inject.Inject
import kotlin.collections.set


/**
 * @author William
 * @date 4/10/21 7:23 PM
 * Class Comment：主入口
 */
@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override val viewBinding: ActivityMainBinding by bindingView()

    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var mAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        // Add a callback that's called when the splash screen is animating to the app content.
        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            val splashScreenView = splashScreenViewProvider.view
            // Create your custom animation.
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView, View.TRANSLATION_Y, 0f, -splashScreenView.height.toFloat()
            )
            slideUp.interpolator = AnticipateInterpolator()
            slideUp.duration = 200L
            // Call SplashScreenView.remove at the end of your custom animation.
            slideUp.doOnEnd { splashScreenViewProvider.remove() }
            // Run your animation.
            slideUp.start()
        }
    }

    override fun initAction() {
        viewBinding.recyclerView.apply {
            adapter = mAdapter
            RecyclerViewDivider.staggeredGrid()
                .includeStartEdge()
                .includeEdge().spacingSize(R.dimen.dp_10.toPx())
                .build().addTo(this)
        }

        viewModel.dataList.observe(this) {
            mAdapter.setList(it)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 获取近期任何进程终止的原因
            val list = activityManager?.getHistoricalProcessExitReasons(packageName, 0, 1)
            list?.forEach { info ->
                info.toString().logE()
            }
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
                    val androidId =
                        Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                    "androidId: $androidId".logI()
                }

                9 -> {
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
                26 -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        createBubble(mActivity)
                    } else {
                        "The device 's android version is below API 30, can't create bubble".toast()
                    }
                }

                27 -> openActivity<TouchImageActivity>(mActivity)
                28 -> openActivity<RoundCornerLayoutActivity>(mActivity)
                29 -> openActivity<MotionLayoutActivity>(mActivity)
                30 -> openActivity<CameraxActivity>(mActivity)
                31 -> openActivity<SideSlipActivity>(mActivity)
                32 -> openActivity<DrawerActivity>(mActivity)
                33 -> openActivity<WebActivity>(mActivity)
                34 -> openActivity<CameraActivity>(mActivity)
                35 -> openActivity<ClockViewActivity>(mActivity)
                36 -> openActivity<SectorViewActivity>(mActivity)
                37 -> openActivity<AlarmManagerActivity>(mActivity)
                38 -> openActivity<GlideActivity>(mActivity)
                39 -> openActivity<ProfilerActivity>(mActivity)
                40 -> openActivity<DataStoreActivity>(mActivity)
                41 -> openActivity<RoundImageActivity>(mActivity)
                42 -> openActivity<MediaPickerActivity>(mActivity)
                43 -> openActivity<CoilActivity>(mActivity)
                44 -> openActivity<RomInfoActivity>(mActivity)
                45 -> openActivity<SlidingPaneActivity>(mActivity)
                else -> {
                }
            }
        }

        createOpNotedCallback()
    }

    private fun createOpNotedCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val appOpsCallback = object : AppOpsManager.OnOpNotedCallback() {
                private fun logPrivateDataAccess(opCode: String, trace: String) {
                    "Private data accessed. Operation: $opCode\nStack Trace:\n$trace".logD()
                }

                override fun onNoted(syncNotedAppOp: SyncNotedAppOp) {
                    logPrivateDataAccess(syncNotedAppOp.op, Throwable().stackTrace.toString())
                }

                override fun onSelfNoted(syncNotedAppOp: SyncNotedAppOp) {
                    logPrivateDataAccess(syncNotedAppOp.op, Throwable().stackTrace.toString())
                }

                override fun onAsyncNoted(asyncNotedAppOp: AsyncNotedAppOp) {
                    logPrivateDataAccess(asyncNotedAppOp.op, asyncNotedAppOp.message)
                }
            }

            val appOpsManager = getSystemService(AppOpsManager::class.java) as AppOpsManager
            appOpsManager.setOnOpNotedCallback(mainExecutor, appOpsCallback)
        }
    }

    private fun showDialog() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("权限申请")
            .setMessage("权限申请内容")
            .setPositiveButton("去授权") { _, _ ->
                "去授权".toast()
            }.setNegativeButton("暂不授权") { _, _ ->
                "暂不授权".toast()
            }.create()
        dialog.show()
    }

    override fun initData() {
        setTitleText("EasyKotlin")

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
            if (map["ek_type"]?.toInt() == 1) {
                openActivity<BubbleActivity>(this)
            }
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

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

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
