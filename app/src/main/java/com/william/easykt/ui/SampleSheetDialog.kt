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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.william.base_component.fragment.BaseBottomSheetDialogFragment
import com.william.base_component.utils.getScreenHeight
import com.william.base_component.utils.logD
import com.william.easykt.databinding.DialogSampleSheetBinding


/**
 * @author William
 * @date 2021/8/20 22:08
 * Class Comment：SampleSheetDialog
 */
class SampleSheetDialog : BaseBottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogSampleSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun initView() {
        addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                "sheet onStateChanged, state: $newState".logD()
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                "sheet onSlide, offset: $slideOffset".logD()
            }

        })
    }

    override fun getHeight() = (getScreenHeight() * 0.8f).toInt()

    override fun getPeekHeight() = (getScreenHeight() * 0.5f).toInt()

    override fun getBehaviorState() = BottomSheetBehavior.STATE_COLLAPSED

    companion object {

        fun show(manager: FragmentManager) {
            SampleSheetDialog().show(manager)
        }
    }

}