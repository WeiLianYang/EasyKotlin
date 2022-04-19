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

package com.william.easykt.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView

class MultiTouchImageView : AppCompatImageView {

    private var mLeft = 0
    private var mTop = 0
    private var mStartX = 0f
    private var mStartY = 0f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = event.x
                mStartY = event.y
                mLeft = left
                mTop = top
            }
            MotionEvent.ACTION_MOVE -> {
                val index = event.findPointerIndex(0)
                if (index == -1) {
                    return false
                }
                mLeft = (mLeft + event.getX(index) - mStartX).toInt()
                mTop = (mTop + event.getY(index) - mStartY).toInt()
                layout(mLeft, mTop, mLeft + width, mTop + height)
            }
            MotionEvent.ACTION_UP -> {}
        }
        return true
    }
}