package com.william.base_component.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.william.base_component.BaseApp.Companion.instance

/**
 * @author William
 * @date 2020/5/22 22:54
 * Class Comment：
 */
/**
 * Show the soft input.
 * 显示软键盘
 *
 * @param activity The activity.
 */
fun showSoftInput(activity: Activity) {
    val imm =
        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            ?: return
    var view = activity.currentFocus
    if (view == null) {
        view = View(activity)
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.requestFocus()
    }
    imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
}

/**
 * Show the soft input.
 *
 * @param view The view.
 */
fun showSoftInput(view: View) {
    val imm =
        instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    view.isFocusable = true
    view.isFocusableInTouchMode = true
    view.requestFocus()
    imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
}

/**
 * Show the soft input using toggle.
 * 显示软键盘用 toggle
 *
 * @param activity The activity.
 */
fun showSoftInputUsingToggle(activity: Activity) {
    if (isSoftInputVisible(activity)) {
        return
    }
    toggleSoftInput()
}

/**
 * Hide the soft input.
 * 隐藏软键盘
 *
 * @param activity The activity.
 */
fun hideSoftInput(activity: Activity) {
    val imm =
        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            ?: return
    var view = activity.currentFocus
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * Hide the soft input.
 *
 * @param view The view.
 */
fun hideSoftInput(view: View) {
    val imm =
        instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * Hide the soft input.
 * 隐藏软键盘用 toggle
 *
 * @param activity The activity.
 */
fun hideSoftInputUsingToggle(activity: Activity) {
    if (!isSoftInputVisible(activity)) {
        return
    }
    toggleSoftInput()
}

/**
 * Toggle the soft input display or not.
 * 切换键盘显示与否状态
 */
fun toggleSoftInput() {
    val imm =
        instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

/**
 * Return whether soft input is visible.
 * 判断软键盘是否可见
 *
 * @param activity The activity.
 * @return `true`: yes<br></br>`false`: no
 */
fun isSoftInputVisible(activity: Activity): Boolean {
    return getDecorViewInvisibleHeight(activity) > 0
}

private var sDecorViewDelta = 0

private fun getDecorViewInvisibleHeight(activity: Activity): Int {
    val decorView = activity.window.decorView
    val outRect = Rect()
    decorView.getWindowVisibleDisplayFrame(outRect)
    Log.d(
        "KeyboardUtils", "getDecorViewInvisibleHeight: "
                + (decorView.bottom - outRect.bottom)
    )
    val delta = Math.abs(decorView.bottom - outRect.bottom)
    if (delta <= getNavBarHeight()) {
        sDecorViewDelta = delta
        return 0
    }
    return delta - sDecorViewDelta
}