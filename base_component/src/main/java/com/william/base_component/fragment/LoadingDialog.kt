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

package com.william.base_component.fragment

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.william.base_component.BaseApp
import com.william.base_component.R
import com.william.base_component.extension.dp

/**
 * @author William
 * @date 2020/4/16 16:53
 * Class Comment：加载弹窗
 */
class LoadingDialog : DialogFragment() {

    var canNotCancel = false

    override fun onStart() {
        super.onStart()
        setStyle(STYLE_NORMAL, 0)

        val window: Window? = dialog?.window
        if (window != null) {
            window.setDimAmount(0f)
            val params = window.attributes
            params.apply {
                width = 80.dp
                height = 80.dp
                gravity = Gravity.CENTER
            }
            window.attributes = params
            window.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    BaseApp.instance,
                    R.drawable.base_icon_loading_bg
                )
            )
        }
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setOnKeyListener { _, keyCode, _ ->
            keyCode == KeyEvent.KEYCODE_BACK && canNotCancel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.base_dialog_loading, container)
    }

    companion object {

        @JvmStatic
        fun newInstance(): LoadingDialog {
            val bundle = Bundle()
            val fragment = LoadingDialog()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            manager.beginTransaction().remove(this).commit()
            super.show(manager, tag)
        } catch (e: Exception) {
        }
    }
}
