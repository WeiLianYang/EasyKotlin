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

package com.william.base_component.net.data

/**
 * @author William
 * @date 2020/4/24 22:22
 * Class Comment：Paging response data model
 */
open class PageListBean<T> {
    var total = 0 // 返回的数据总数
    var page = 0 // 当前页码
    var size = 0 // 当前页码返回的数据数
    var list: MutableList<T>? = null
}