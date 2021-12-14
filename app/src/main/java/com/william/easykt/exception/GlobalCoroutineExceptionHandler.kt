package com.william.easykt.exception

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * author : WilliamYang
 * date : 2021/12/14 15:17
 * description : 全局 协程 异常处理。只是未了获取异常信息，并不能阻止程序崩溃
 */
class GlobalCoroutineExceptionHandler : CoroutineExceptionHandler {
    override val key = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        Timber.e(exception, "un handled Coroutine Exception with ${context[Job]}")
    }
}