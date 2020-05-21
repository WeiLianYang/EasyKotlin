package com.william.base_component

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.william.base_component.manager.ActivityStackManager


/**
 * @author William
 * @date 2020/4/16 17:24
 * Class Comment：Application context
 */
class BaseApp : Application() {

    lateinit var mContext: Context
    private var mActivityCount = 0

    override fun onCreate() {
        super.onCreate()
        mContext = this

        initConfig()
        registerLifecycleCallbacks()
    }

    private object InstanceHelper {
        val instance = BaseApp()
    }

    companion object {

        const val TAG = "BaseApp"

        @JvmStatic
        val instance: BaseApp
            get() = InstanceHelper.instance

    }

    private fun registerLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                ActivityStackManager.addActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {
                mActivityCount++
            }

            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {
                mActivityCount--
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {
                ActivityStackManager.endActivity(activity)
            }
        })
    }

    fun isAppInBackground(): Boolean = mActivityCount == 0

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    /**
     * 初始化配置
     */
    private fun initConfig() {
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