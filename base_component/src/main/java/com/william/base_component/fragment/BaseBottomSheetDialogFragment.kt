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

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.william.base_component.R

/**
 * @author William
 * @date 2020/5/1 18:10
 * Class Comment：
 */
abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private var behavior: BottomSheetBehavior<FrameLayout>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return if (context == null) {
            super.onCreateDialog(savedInstanceState)
        } else BottomSheetDialog(requireContext(), R.style.BottomSheetStyle)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        val dialog = dialog as? BottomSheetDialog
        val bottomSheet = dialog?.findViewById<FrameLayout>(R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            val params =
                bottomSheet.layoutParams as CoordinatorLayout.LayoutParams
            params.height = getHeight()
            behavior = BottomSheetBehavior.from(bottomSheet).apply {
                state = getBehaviorState()
                peekHeight = this@BaseBottomSheetDialogFragment.getPeekHeight()
            }
        }

        initView()
    }

    open fun getHeight() = WindowManager.LayoutParams.WRAP_CONTENT

    open fun getPeekHeight() = 0

    open fun getBehaviorState() = BottomSheetBehavior.STATE_EXPANDED

    fun addBottomSheetCallback(callback: BottomSheetBehavior.BottomSheetCallback) {
        behavior?.addBottomSheetCallback(callback)
    }

    abstract fun initView()

    open fun show(manager: FragmentManager) {
        super.show(manager, this.javaClass.simpleName)
    }

}