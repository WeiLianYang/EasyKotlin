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

package com.william.base_component.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.william.base_component.databinding.BaseTitleLayoutBinding

/**
 *  author : WilliamYang
 *  date : 2021/7/28 13:51
 *  description : 标题栏
 */
class TitleBarLayout : ConstraintLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initLayout(context)
    }

    private lateinit var binding: BaseTitleLayoutBinding

    var leftIconClickListener: (v: View) -> Unit = {}
    var rightIconClickListener: (v: View) -> Unit = {}
    var rightTextClickListener: (v: View) -> Unit = {}

    private fun initLayout(context: Context) {
        binding = BaseTitleLayoutBinding.inflate(LayoutInflater.from(context), this)

        binding.ivBaseTitleLeft.setOnClickListener {
            leftIconClickListener.invoke(it)
        }
        binding.ivBaseTitleRight.setOnClickListener {
            rightIconClickListener.invoke(it)
        }
        binding.tvBaseTitleRight.setOnClickListener {
            rightTextClickListener.invoke(it)
        }

    }

    fun setTitle(text: String?) {
        binding.tvBaseTitleText.text = text
    }

    fun setLeftImageRes(resId: Int) {
        binding.ivBaseTitleLeft.setImageResource(resId)
    }

    fun setRightImageRes(resId: Int) {
        binding.ivBaseTitleRight.setImageResource(resId)
    }

    fun setRightText(text: String?) {
        binding.tvBaseTitleRight.text = text
    }

    fun setLineVisible(visible: Boolean) {
        binding.viewBaseLine.visibility = if (visible) View.VISIBLE else View.GONE
    }

}