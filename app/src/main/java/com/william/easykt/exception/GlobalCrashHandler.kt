package com.william.easykt.exception

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import timber.log.Timber
import java.io.*
import java.util.*
import kotlin.concurrent.thread
import kotlin.system.exitProcess

/**
 * author : WilliamYang
 * date : 2021/12/14 15:20
 * description : 全局线程未捕获异常处理
 */
object GlobalCrashHandler : Thread.UncaughtExceptionHandler {

    private var defaultHandler: Thread.UncaughtExceptionHandler? = null
    private lateinit var application: Application
    private val paramsMap: MutableMap<String, String> = HashMap()

    private val TAG = this.javaClass.simpleName

    fun init(context: Application) {
        application = context
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (!handleException(ex) && defaultHandler != null) {
            // 如果自己没处理交给系统处理
            defaultHandler?.uncaughtException(thread, ex)
        } else {
            // 自行处理
            try { // 延迟N秒杀进程
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                Timber.e(e, "error")
            }
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(0)
        }
    }

    /**
     * 收集错误信息.发送到服务器
     *
     * @return 处理了该异常返回true, 否则false
     */
    private fun handleException(ex: Throwable?): Boolean {
        ex ?: return false
        collectInfo(application)
        thread {
            saveCrashInfoToFile(ex)
        }
        return true
    }

    private fun collectInfo(context: Context) {
        try {
            val manager = context.packageManager
            val packageInfo =
                manager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
            if (packageInfo != null) {
                paramsMap["versionName"] = packageInfo.versionName ?: ""
                paramsMap["versionCode"] = packageInfo.versionCode.toString()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e(e, "an error occurred when collect package info")
        }
        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                paramsMap[field.name] = field[null]?.toString() ?: ""
            } catch (e: Exception) {
                Timber.e(e, "an error occurred when collect crash info")
            }
        }
    }

    private fun saveCrashInfoToFile(ex: Throwable): String? {
        val sb = StringBuffer()
        for ((key, value) in paramsMap) {
            sb.append("$key=$value\n")
        }
        val writer: Writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        sb.append(result)
        try {
            val fileName = "Crash_${System.currentTimeMillis()}.log"
            val path = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                application.getExternalFilesDir(null)?.path
            } else {
                application.cacheDir
            }

//            application.openFileOutput(fileName, Context.MODE_PRIVATE).use {
//                it.write(sb.toString().toByteArray())
//                Timber.i(TAG, "saveCrashInfoToFile: $sb")
//            }

            val filePath = "$path/crash/"
            val file = File(filePath)
            if (!file.exists()) {
                file.mkdirs()
            }

            FileOutputStream("$filePath$fileName").use {
                it.write(sb.toString().toByteArray())
                Timber.i(TAG, "saveCrashInfoToFile: $sb")
            }
            return fileName
        } catch (e: Exception) {
            Timber.e(e, "an error occurred while writing file...")
        }
        return null
    }
}