/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.william.easykt

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UiAutomator 测试 侧滑页面
 */
@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class SlidePageTest {
    private var device: UiDevice? = null

    @Before
    fun startMainActivityFromHomeScreen() {
        // 初始化 UiDevice
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // 打开主页
        device?.pressHome()

        val launcherPackage = launcherPackageName
        assertThat(launcherPackage, CoreMatchers.notNullValue())
        device?.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT.toLong())

        // 打开APP
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(APP_PACKAGE)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)

        // 等待 APP 被打开
        device?.wait(Until.hasObject(By.pkg(APP_PACKAGE).depth(0)), LAUNCH_TIMEOUT.toLong())
    }

    @Test
    fun checkPreconditions() {
        assertThat(device, CoreMatchers.notNullValue())
    }

    @Test
    fun testContentText() {
        // 首先滚动到需要匹配的位置，然后点击。
        device?.findObject(By.res(APP_PACKAGE, "recyclerView"))?.scroll(Direction.DOWN, 0.5f, 1000)
        device?.findObject(By.text("Side slip Layout"))?.click()

        /** 侧滑拖拽，如果当前 系统手势 设置的 不是 底部3建导航方式，会导致后续的检测会失败 */
        val obj = device?.wait(Until.findObject(By.res(APP_PACKAGE, "slideLayout")), 500)
        obj?.swipe(Direction.RIGHT, 0.5f, 500)

        val text = device?.wait(Until.findObject(By.res(APP_PACKAGE, "slide_content")), 500)
        assertThat(text?.text, `is`(equalTo("Content View")))
    }

    /**
     * 使用包管理器查找设备启动器的包名称。
     * 通常这个包是“com.android.launcher”，但有时可能会有所不同。这是适用于所有平台的通用解决方案。
     */
    private val launcherPackageName: String
        get() {
            // launcher Intent
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)

            val pm = ApplicationProvider.getApplicationContext<Context>().packageManager
            val resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
            return resolveInfo?.activityInfo?.packageName ?: ""
        }

    companion object {
        private const val APP_PACKAGE = "com.william.easykt"
        private const val LAUNCH_TIMEOUT = 5000
    }
}