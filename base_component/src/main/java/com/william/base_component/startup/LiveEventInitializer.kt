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

package com.william.base_component.startup

import android.content.Context
import androidx.startup.Initializer
import com.jeremyliao.liveeventbus.LiveEventBus
import com.william.base_component.extension.logD


/**
 * @author William
 * @date 2022/5/15 11:11
 * Class Comment：LiveEventBus Initializer
 */
class LiveEventInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        // 配置LifecycleObserver（如Activity）接收消息的模式（默认值true）：
        // true：整个生命周期（从onCreate到onDestroy）都可以实时收到消息
        // false：激活状态（Started）可以实时收到消息，非激活状态（Stoped）无法实时收到消息，需等到Activity重新变成激活状态，方可收到消息
        // 更多配置参照 https://github.com/JeremyLiao/LiveEventBus/blob/master/docs/config.md
        LiveEventBus.config().lifecycleObserverAlwaysActive(false)
        "LiveEventBus init".logD()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

}