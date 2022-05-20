package com.william.easykt.utils

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import com.william.base_component.BaseApp
import com.william.easykt.BuildConfig

/**
 * author：William
 * date：2022/5/20 11:14
 * description：WebView 工具类
 */
const val LOAD_URL = 0 // 加载链接
const val LOAD_DATA = 1 // 加载富文本
const val RELOAD_PAGE = 2 // 重新加载链接

/**
 * 创建 WebView 并添加到 父View 中，设置 LayoutParams，并设置 WebSettings
 * @param parentView 父View
 * @param layoutParams layoutParams
 * @return WebView
 */
fun createWebView(
    parentView: ViewGroup? = null,
    layoutParams: ViewGroup.LayoutParams? = null
): WebView {
    val webView = WebView(BaseApp.instance)
    layoutParams?.let {
        webView.layoutParams = it
    }
    webView.setupWebSettings()
    parentView?.addView(webView)
    return webView
}

/**
 * 设置WebSettings
 */
@SuppressLint("SetJavaScriptEnabled")
fun WebView?.setupWebSettings() {
    this?.settings?.apply {
        javaScriptEnabled = true
        cacheMode = WebSettings.LOAD_NO_CACHE
        userAgentString = "$userAgentString platform/app"
        javaScriptCanOpenWindowsAutomatically = true
        // 开启LocalStorage缓存权限
        allowFileAccess = true
        domStorageEnabled = true
        // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
    }
}

/**
 * 加载某个网页：loadUrl("http://www.google.com/");
 * 加载apk包中的html页面：loadUrl("file:///android_asset/error.html");
 * 加载手机本地的html页面：loadUrl("content://com.android.htmlfileprovider/sdcard/test.html");
 * 加载富文本：loadData(content, "text/html;charset=UTF-8", "utf-8")
 *
 * @param content 加载内容，可能是网页，html，富文本
 * @param type type 0：加载链接，1：加载富文本，2：重新加载链接
 */
fun WebView?.loadContent(content: String?, type: Int = LOAD_URL) {
    content ?: return
    when (type) {
        LOAD_URL -> {
            // 加载链接
            this?.loadUrl(content)
        }
        LOAD_DATA -> {
            // 加载富文本
            this?.apply {
                clearCache(true)
                clearFormData()
                loadData(content, "text/html;charset=UTF-8", "utf-8")
            }
        }
        RELOAD_PAGE -> {
            // 重新加载链接
            this?.apply {
                loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
                loadUrl(content)
            }
        }
    }
}

/**
 * 设置WebView内容可调试，配合Chrome浏览器的调试工具使用。
 */
fun setWebViewDebuggingEnabled() {
    WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
}

/**
 * 处理WebView的返回上一页的操作，如果没有上一页，则返回false，交给父视图处理
 */
fun WebView?.onWebKeyDown(keyCode: Int): Boolean {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
        return this?.onWebGoBack() ?: false
    }
    return false
}

/**
 * 返回Web的上一页，如果执行了返回上一页，则返回true，否则返回false
 */
fun WebView?.onWebGoBack(): Boolean {
    if (this?.canGoBack() == true) {
        this.goBack()
        return true
    }
    return false
}

/**
 * 销毁 WebView，在 onDestroy 中调用
 */
fun WebView?.destroy() {
    this?.let {
        it.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        it.clearHistory()
        (it.parent as? ViewGroup)?.removeView(it)
        it.removeAllViews()
        it.destroy()
    }
}
