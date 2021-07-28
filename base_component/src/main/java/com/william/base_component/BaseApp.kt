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

package com.william.base_component

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
import com.flurry.android.FlurryAgent
import com.jeremyliao.liveeventbus.LiveEventBus
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.william.base_component.manager.ActivityStackManager
import com.william.base_component.manager.KVStoreManager
import kotlin.properties.Delegates


/**
 * @author William
 * @date 2020/4/16 17:24
 * Class Comment：Application context
 */
class BaseApp : Application() {

    companion object {

        const val TAG = "BaseApp"
        const val FLURRY_KEY = "ZPTYYTJZ8RBDFDQYNNGC"

        @JvmStatic
        var instance: Context by Delegates.notNull()
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = applicationContext

        initFlurryAnalytics()

        KVStoreManager.init(this)

        initLoggerConfig()
        initLiveEventConfig()
        registerLifecycleCallbacks()
    }

    private fun initFlurryAnalytics() {
        FlurryAgent.Builder()
            .withLogEnabled(BuildConfig.DEBUG)
            .withCaptureUncaughtExceptions(true)
            .build(this, FLURRY_KEY)
    }

    private fun initLiveEventConfig() {
        // 配置LifecycleObserver（如Activity）接收消息的模式（默认值true）：
        // true：整个生命周期（从onCreate到onDestroy）都可以实时收到消息
        // false：激活状态（Started）可以实时收到消息，非激活状态（Stoped）无法实时收到消息，需等到Activity重新变成激活状态，方可收到消息
        // 更多配置参照 https://github.com/JeremyLiao/LiveEventBus/blob/master/docs/config.md
        LiveEventBus.config().lifecycleObserverAlwaysActive(false)
    }

    private fun registerLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                ActivityStackManager.addActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {
                FlurryAgent.onStartSession(activity)
            }

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {
                FlurryAgent.onEndSession(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {
                ActivityStackManager.endActivity(activity)
            }
        })
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    /**
     * 初始化配置
     */
    private fun initLoggerConfig() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)  // 隐藏线程信息 默认：显示
            .methodCount(1)         // 决定打印多少行（每一行代表一个方法）默认：2
            .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
            .tag(TAG)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }
}