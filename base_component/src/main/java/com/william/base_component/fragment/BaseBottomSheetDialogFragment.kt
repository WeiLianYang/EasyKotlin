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
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.william.base_component.R
import kotlin.math.abs

/**
 * @author William
 * @date 2020/5/1 18:10
 * Class Comment：
 */
open class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private var behavior: BottomSheetBehavior<FrameLayout>? = null
    private var state: Int? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return if (context == null) {
            super.onCreateDialog(savedInstanceState)
        } else BottomSheetDialog(context!!, R.style.BottomSheetStyle)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        val dialog = dialog as? BottomSheetDialog
        val bottomSheet =
            dialog?.delegate?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            val layoutParams =
                bottomSheet.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = getHeight()
            behavior = BottomSheetBehavior.from(bottomSheet)
            behavior?.state = BottomSheetBehavior.STATE_EXPANDED
            behavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // [0,-1]
                    if (state == 2 && abs(slideOffset) <= 0.5f) {
                        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    state = newState
                }

            })
        }

    }

    open fun getHeight() = WindowManager.LayoutParams.WRAP_CONTENT

}