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

package com.william.easykt.ui

import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.base_component.utils.openBrowser
import com.william.easykt.databinding.ActivityIntentDispatcherBinding


/**
 * ### author : WilliamYang
 * ### date : 2023/7/14 18:06
 * ### description : 打开指定页面
 */
class IntentDispatcherActivity : BaseActivity() {

    override val viewBinding: ActivityIntentDispatcherBinding by bindingView()

    override fun initAction() {
    }

    override fun initData() {
        setTitleText(javaClass.simpleName)

        viewBinding.tvButton1.setOnClickListener {
            openBrowser(
                this,
                "https://www.baidu.com",
                "com.android.chrome",
                "com.google.android.apps.chrome.Main"
            )
        }

        viewBinding.tvButton2.setOnClickListener {
            openBrowser(
                this,
                "https://www.baidu.com",
                "com.android.chrome",
                "com.google.android.apps.chrome.IntentDispatcher"
            )
        }
    }
}