package com.william.easykt

import com.william.base_component.BaseApp
import com.william.easykt.exception.GlobalCrashHandler

/**
 *  author : WilliamYang
 *  date : 2021/12/14 15:23
 *  description :
 */
class App : BaseApp() {

    override fun onCreate() {
        super.onCreate()

        GlobalCrashHandler.init(this)
    }
}