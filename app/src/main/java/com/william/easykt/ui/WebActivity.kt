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

import android.view.KeyEvent
import android.webkit.WebView
import androidx.constraintlayout.widget.ConstraintLayout
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.easykt.R
import com.william.easykt.databinding.ActivityWebBinding
import com.william.easykt.utils.createWebView
import com.william.easykt.utils.loadContent
import com.william.easykt.utils.onWebGoBack
import com.william.easykt.utils.onWebKeyDown


/**
 * @author William
 * @date 2022/5/20 11:10
 * Class Comment：Test web page
 */
class WebActivity : BaseActivity() {

    override val viewBinding by bindingView<ActivityWebBinding>()

    private lateinit var webView: WebView

    override fun initView() {
        super.initView()

        webView = createWebView(
            viewBinding.root,
            ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        )

        webView.loadContent("https://www.baidu.com/")

    }

    override fun initData() {
        setTitleText(R.string.test_web)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (webView.onWebKeyDown(keyCode)) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        if (!webView.onWebGoBack()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }

}