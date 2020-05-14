package com.william.base_component.manager

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager


/**
 * @author William
 * @date 2020/4/21 19:53
 * Class Comment：status bar manager
 */
object StatusBarManager {

    fun setStatusBar(activity: Activity, darkMode: Boolean) {
        val window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT

        val tintManager =
            SystemBarTintManager(activity)
        tintManager.isStatusBarTintEnabled = true
        tintManager.setTintAlpha(0f)

        setMIUIStatusBarFont(activity, darkMode)
        setMeiZuStatusBarFont(activity, darkMode)
        setDefaultStatusBarFont(activity, darkMode)
    }

    @SuppressLint("PrivateApi")
    private fun setMIUIStatusBarFont(activity: Activity, darkMode: Boolean) {
        val window = activity.window
        val clazz: Class<*> = window.javaClass

        try {
            val cla = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = cla.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag = field.getInt(cla)
            val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.java, Int::class.java)
            if (darkMode) {
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag)
            }
        } catch (e: Exception) {
        }
    }

    private fun setMeiZuStatusBarFont(activity: Activity, dark: Boolean) {
        try {
            val lp = activity.window.attributes
            val darkFlag =
                WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags =
                WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            value = if (dark) {
                value or bit
            } else {
                value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            activity.window.attributes = lp
        } catch (ignored: java.lang.Exception) {
        }
    }

    private fun setDefaultStatusBarFont(
        activity: Activity,
        dark: Boolean
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = activity.window
            val decorView = window.decorView
            if (dark) {
                decorView.systemUiVisibility =
                    decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility =
                    decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
    }

}