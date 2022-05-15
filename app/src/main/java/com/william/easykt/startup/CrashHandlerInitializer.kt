/*
 * Copyright WeiLianYang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.william.easykt.startup

import android.content.Context
import androidx.startup.Initializer
import com.william.base_component.extension.logD
import com.william.easykt.exception.GlobalCrashHandler


/**
 * @author William
 * @date 2022/5/15 14:58
 * Class Comment：全局异常处理初始化器
 */
class CrashHandlerInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        GlobalCrashHandler.init(context)
        "GlobalCrashHandler init".logD()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}