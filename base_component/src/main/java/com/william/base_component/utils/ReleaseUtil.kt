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

package com.william.base_component.utils


/**
 * @author William
 * @date 2020/5/13 15:56
 * Class Comment：
 */


private const val DEBUG_URL = "https://www.wanandroid.com/"
private const val RELEASE_URL = "https://"

const val IS_RELEASE: Boolean = false

val apiBaseUrl = if (IS_RELEASE) {
    RELEASE_URL
} else {
    DEBUG_URL
}