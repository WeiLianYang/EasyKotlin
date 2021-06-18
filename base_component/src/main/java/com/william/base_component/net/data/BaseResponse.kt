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
 * @date 2020/4/19 11:18
 * Class Comment：BaseResponse
 */
open class BaseResponse<T>(
    var errorCode: Int? = 0,// 业务码
    var errorMsg: String? = null,// 异常信息
    val data: T// 业务数据
)