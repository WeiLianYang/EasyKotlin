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
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.easykt.R
import com.william.easykt.data.SectorBean
import com.william.easykt.databinding.ActivitySectorViewBinding


/**
 * author : WilliamYang
 * date : 2022/6/30 11:56
 * description : 多彩扇形
 */
class SectorViewActivity : BaseActivity() {

    override val viewBinding: ActivitySectorViewBinding by bindingView()

    override fun initData() {
        setTitleText(R.string.test_sector_view)

        val list = arrayListOf(
            SectorBean(Color.RED, 45f),
            SectorBean(Color.rgb(255, 192, 203), 45f),
            SectorBean(Color.YELLOW, 45f),
            SectorBean(Color.GREEN, 45f),
            SectorBean(Color.CYAN, 45f),
            SectorBean(Color.BLUE, 45f),
            SectorBean(Color.rgb(128, 0, 128), 45f),
            SectorBean(Color.BLACK, 45f)
        )

        play1(list)

        play2(list)

        viewBinding.button1.setOnClickListener {
            play1(list)
        }

        viewBinding.button2.setOnClickListener {
            play2(list)
        }
    }

    private fun play1(list: List<SectorBean>) {
        viewBinding.sector1.setData(list)
        viewBinding.sector1.startAnimation(500)
    }

    private fun play2(list: List<SectorBean>) {
        viewBinding.sector2.setData(list.reversed())
        viewBinding.sector2.startAnimation(500)
    }
}