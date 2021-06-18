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

import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.text.ParseException
import javax.net.ssl.SSLHandshakeException

/**
 * handle network exception, return ApiException
 */
fun handleException(e: Throwable): ApiException? {
    val ex: ApiException
    return if (e is HttpException) {
        ex = ApiException(e, HTTP_ERROR)
        when (e.code()) {
            UNAUTHORIZED -> {
                ex.message = "无授权"
                ex.code = 401
            }
            FORBIDDEN -> ex.message = "服务禁止访问"
            NOT_FOUND -> ex.message = "服务不存在"
            REQUEST_TIMEOUT -> ex.message = "请求超时"
            GATEWAY_TIMEOUT -> ex.message = "网关超时"
            INTERNAL_SERVER_ERROR -> ex.message = "服务器内部错误"
            BAD_GATEWAY -> {
            }
            SERVICE_UNAVAILABLE -> {
            }
            else -> ex.message = "网络错误"
        }
        ex.message += ":" + e.message
        ex
    } else if (e is JsonParseException
        || e is JSONException
        || e is ParseException
    ) {
        ex = ApiException(e, PARSE_ERROR)
        ex.message = "解析错误"
        ex
    } else if (e is ConnectException) {
        ex = ApiException(e, NETWORK_ERROR)
        ex.message = "连接失败"
        ex
    } else if (e is SSLHandshakeException) {
        ex = ApiException(e, SSL_ERROR)
        ex.message = "证书验证失败"
        ex
    } else {
        ex = ApiException(e, UNKNOWN)
        ex.message = "未知错误" + ":" + e.message
        ex
    }
}

class ApiException(throwable: Throwable?, var code: Int) : Exception(throwable) {
    override var message: String? = null
}