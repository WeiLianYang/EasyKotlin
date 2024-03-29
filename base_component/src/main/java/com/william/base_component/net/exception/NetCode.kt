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

package com.william.base_component.net.exception

/**
 * @author William
 * @date 2020/4/22 12:04
 * Class Comment：响应码
 */
/**
 * 响应成功
 */
const val SUCCESS = 0

const val TOKEN_EXPIRED = -1;

const val UNAUTHORIZED = 401
const val FORBIDDEN = 403
const val NOT_FOUND = 404
const val REQUEST_TIMEOUT = 408
const val INTERNAL_SERVER_ERROR = 500
const val BAD_GATEWAY = 502
const val SERVICE_UNAVAILABLE = 503
const val GATEWAY_TIMEOUT = 504

/**
 * 协议出错
 */
const val HTTP_ERROR = 1003

/**
 * 未知错误
 */
const val UNKNOWN = 1000

/**
 * 解析错误
 */
const val PARSE_ERROR = 1001

/**
 * 网络错误
 */
const val NETWORK_ERROR = 1002

/**
 * 证书出错
 */
const val SSL_ERROR = 1005