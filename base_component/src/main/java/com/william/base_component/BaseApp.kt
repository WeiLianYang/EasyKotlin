package com.william.base_component

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
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

        registerLifecycleCallbacks()
    }

    private object InstanceHelper {
        val instance = BaseApp()
    }

    companion object {

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
}