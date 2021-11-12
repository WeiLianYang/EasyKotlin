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

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding

/**
 * @author William
 * @date 2020/5/8 12:25
 * Class Comment：DialogFragment
 */
abstract class BaseDialogFragment : DialogFragment() {

    protected var mFragment: Fragment? = null
    protected var mActivity: FragmentActivity? = null

    protected abstract val viewBinding: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = viewBinding.root
        mFragment = this
        mActivity = activity
        initView(view)
        initAction()
        initData()
        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            attributes.apply {
                width = if (getWidth() != 0) getWidth() else WindowManager.LayoutParams.WRAP_CONTENT
                height =
                    if (getHeight() != 0) getHeight() else WindowManager.LayoutParams.WRAP_CONTENT
                gravity = getGravity()
            }
            setBackgroundDrawable(ColorDrawable(0x00000000))
        }
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun initView(view: View)

    protected abstract fun initAction()

    protected abstract fun initData()

    protected open fun getWidth() = WindowManager.LayoutParams.WRAP_CONTENT

    protected open fun getHeight() = WindowManager.LayoutParams.WRAP_CONTENT

    protected open fun getTopMargin() = 0

    protected open fun getGravity() = Gravity.CENTER

}