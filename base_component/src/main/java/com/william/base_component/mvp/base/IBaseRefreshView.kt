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

package com.william.base_component.mvp.base


/**
 * @author William
 * @date 2020/5/17 20:09
 * Class Comment：
 */
interface IBaseRefreshView : IBaseView {

    /**
     * 设置刷新后的状态
     * @param success 刷新结果
     */
    fun setupRefreshStatus(success: Boolean)

    /**
     * 设置加载更多后的状态
     * @param success 刷新结果
     * @param hasMoreData 有更多可以加载的数据，默认为true
     */
    fun setupLoadMoreStatus(success: Boolean, hasMoreData: Boolean = true)

}