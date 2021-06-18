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

import android.graphics.Color
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.william.base_component.BaseApp
import com.william.base_component.extension.dp
import com.william.base_component.utils.setClipViewCornerRadius
import com.william.easykt.R
import com.william.easykt.data.BannerColorBean
import kotlin.math.abs


/**
 * @author William
 * @date 2020/6/28 14:38
 * Class Comment：层叠卡片转换器
 */
class CascadingCardPageTransformer(private val viewPager: ViewPager) : ViewPager.PageTransformer {

    /**
     * 偏移量
     */
    private var mScaleOffset = 20.dp

    /**
     * 缩放步长的比例
     */
    private val scaleStep = 0.05f

    private var secondForeground: View? = null
    private var thirdForeground: View? = null

    private var secondColorBean: BannerColorBean =
        BannerColorBean(BaseApp.instance.getColor(R.color.color_464646))
    private var thirdColorBean: BannerColorBean =
        BannerColorBean(BaseApp.instance.getColor(R.color.color_5f5f5f))

    override fun transformPage(page: View, position: Float) {
        if (position <= 0.0f) {
            // 在左边已被滑出去的的和正在被滑动的那张卡片
            page.translationX = page.width / 4 * position
            page.translationY = 0f

            page.scaleX = 1f
            page.scaleY = 1f

            page.rotation = position * 60
            // 如果要设置被滑动页面的透明度就放开下面的注释代码
//            page.alpha = 1 - abs(position)
            page.alpha = 1f
            page.findViewById<View>(R.id.page_foreground)?.alpha = 0f
        } else {
            // 在当前卡片下面被遮挡的卡片
            page.translationX = -page.width * position
            page.translationY = mScaleOffset * position

            val scaleFactor = 1f - position * scaleStep
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor

            page.rotation = 0f
            page.alpha = 1f

            if (position <= 1) {
                // 第二张position的变化过程是1->0
                secondForeground = page.findViewById(R.id.page_foreground)
                secondForeground?.isSelected = true
                secondForeground?.alpha = position
            } else if (position <= 2) {
                // 第三张position的变化过程是2->1
                thirdForeground = page.findViewById(R.id.page_foreground)
                thirdForeground?.isSelected = false

                val offset = position - 1
                val alpha =
                    secondColorBean.alpha + (thirdColorBean.alpha - secondColorBean.alpha) * offset
                val red = secondColorBean.red + (thirdColorBean.red - secondColorBean.red) * offset
                val green =
                    secondColorBean.green + (thirdColorBean.green - secondColorBean.green) * offset
                val blue =
                    secondColorBean.blue + (thirdColorBean.blue - secondColorBean.blue) * offset
                thirdForeground?.setBackgroundColor(
                    Color.argb(
                        alpha.toInt(),
                        red.toInt(),
                        green.toInt(),
                        blue.toInt()
                    )
                )
                setClipViewCornerRadius(thirdForeground, 18)
            }
        }
        if (position > viewPager.offscreenPageLimit - 1) {
            // 最后一个卡片即将滑进来或者划出去时的透明度
            page.alpha = 1 - abs(position - (viewPager.offscreenPageLimit - 1))
            page.findViewById<View>(R.id.page_foreground)?.alpha = 1f
        }
    }

}
