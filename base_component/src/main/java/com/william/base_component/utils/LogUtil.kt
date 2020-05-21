package com.william.base_component.utils

import android.util.Log
import com.william.base_component.BuildConfig

/**
 * Logging helper class.
 */
class LogUtil private constructor(private val mClassName: String) {

    private val functionName: String?
        get() {
            val sts =
                Thread.currentThread().stackTrace ?: return null
            for (st in sts) {
                if (st.isNativeMethod) {
                    continue
                }
                if (st.className == Thread::class.java.name) {
                    continue
                }
                if (st.className == this.javaClass.name) {
                    continue
                }
                return (mClassName + "[ " + Thread.currentThread().name + ": "
                        + st.fileName + ":" + st.lineNumber + " "
                        + st.methodName + " ]")
            }
            return null
        }

    companion object {
        var OPEN_LOG = BuildConfig.DEBUG
        var DEBUG = BuildConfig.DEBUG
        private const val TAG = "EasyKotlin:"
        private var log: LogUtil? = null
        private const val USER_NAME = "@tool@"
        fun i(str: Any) {
            print(Log.INFO, str)
        }

        fun d(str: Any) {
            print(Log.DEBUG, str)
        }

        fun v(str: Any) {
            print(Log.VERBOSE, str)
        }

        fun w(str: Any) {
            print(Log.WARN, str)
        }

        fun e(str: Any) {
            print(Log.ERROR, str)
        }

        private fun print(index: Int, s: Any) {
            var str = s
            if (!OPEN_LOG) {
                return
            }
            if (log == null) {
                log = LogUtil(USER_NAME)
            }
            val name = log!!.functionName
            if (name != null) {
                str = "$name - $str"
            }
            if (!DEBUG) {
                if (index <= Log.DEBUG) {
                    return
                }
            }
            when (index) {
                Log.VERBOSE -> Log.v(
                    TAG,
                    str.toString()
                )
                Log.DEBUG -> Log.d(
                    TAG,
                    str.toString()
                )
                Log.INFO -> Log.i(
                    TAG,
                    str.toString()
                )
                Log.WARN -> Log.w(
                    TAG,
                    str.toString()
                )
                Log.ERROR -> Log.e(
                    TAG,
                    str.toString()
                )
                else -> {
                }
            }
        }
    }

}