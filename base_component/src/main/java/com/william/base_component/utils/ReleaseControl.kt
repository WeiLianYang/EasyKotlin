package com.william.base_component.utils


/**
 * @author William
 * @date 2020/5/13 15:56
 * Class Comment：
 */
object ReleaseControl {

    const val IS_RELEASE: Boolean = false

    private const val DEBUG_URL = "https://www.wanandroid.com/"
    private const val RELEASE_URL = "https://"

    val baseUrl = if (IS_RELEASE) {
        RELEASE_URL
    } else {
        DEBUG_URL
    }

}