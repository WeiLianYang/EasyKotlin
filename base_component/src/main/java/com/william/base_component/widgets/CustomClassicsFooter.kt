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
import android.view.View
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.william.base_component.BaseApp
import com.william.base_component.R

/**
 * @author William
 * @date 2020/5/21 19:45
 * Class Comment：custom footer
 */
class CustomClassicsFooter : ClassicsFooter {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    /**
     * 设置数据全部加载完成，将不能再次触发加载功能
     */
    override fun setNoMoreData(noMoreData: Boolean): Boolean {
        if (mNoMoreData != noMoreData) {
            mNoMoreData = noMoreData
            val arrowView: View = mArrowView
            if (noMoreData) {
                mTitleText.text = "已经到底了～"
                arrowView.visibility = View.GONE
            } else {
                mTitleText.text = mTextPulling
                mTitleText.background = null
                mTitleText.setTextColor(
                    BaseApp.instance.getColor(R.color.color_232323)
                )
                arrowView.visibility = View.VISIBLE
            }
        }
        return true
    }
}