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

package com.william.easykt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.william.easykt.data.DiffUtilBean
import kotlinx.coroutines.flow.flowOf


/**
 * @author William
 * @date 2021/8/27 22:19
 * Class Comment：
 */
class DiffUtilViewModel : ViewModel() {

    val listData = flowOf(getList()).asLiveData()

    fun getList(type: Int = 0): List<DiffUtilBean> {
        val list = arrayListOf<DiffUtilBean>()
        for (index in 0..10) {
            if (type == 1) {
                if (index == 1) {
                    list.add(
                        DiffUtilBean(index, "New title: 第 $index 条数据", "New content: 第 $index 条内容")
                    )
                    continue
                }
            } else if (type == 2) {
                if (index == 0 || index == 2 || index == 3) {
                    continue
                }
            }
            list.add(DiffUtilBean(index, "第 $index 条数据", "第 $index 条内容"))
        }
        return list
    }

}