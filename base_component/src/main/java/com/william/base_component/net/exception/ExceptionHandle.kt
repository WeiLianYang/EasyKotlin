package com.william.base_component.net.exception

import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.text.ParseException
import javax.net.ssl.SSLHandshakeException


class ExceptionHandle {

    companion object {

        fun handleException(e: Throwable): ApiException? {
            val ex: ApiException
            return if (e is HttpException) {
                ex = ApiException(e, NetCode.HTTP_ERROR)
                when (e.code()) {
                    NetCode.UNAUTHORIZED -> {
                        ex.message = "无授权"
                        ex.code = 401
                    }
                    NetCode.FORBIDDEN -> ex.message = "服务禁止访问"
                    NetCode.NOT_FOUND -> ex.message = "服务不存在"
                    NetCode.REQUEST_TIMEOUT -> ex.message = "请求超时"
                    NetCode.GATEWAY_TIMEOUT -> ex.message = "网关超时"
                    NetCode.INTERNAL_SERVER_ERROR -> ex.message = "服务器内部错误"
                    NetCode.BAD_GATEWAY -> {
                    }
                    NetCode.SERVICE_UNAVAILABLE -> {
                    }
                    else -> ex.message = "网络错误"
                }
                ex.message += ":" + e.message
                ex
            } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException
            ) {
                ex = ApiException(e, NetCode.PARSE_ERROR)
                ex.message = "解析错误"
                ex
            } else if (e is ConnectException) {
                ex = ApiException(e, NetCode.NETWORK_ERROR)
                ex.message = "连接失败"
                ex
            } else if (e is SSLHandshakeException) {
                ex = ApiException(e, NetCode.SSL_ERROR)
                ex.message = "证书验证失败"
                ex
            } else {
                ex = ApiException(e, NetCode.UNKNOWN)
                ex.message = "未知错误" + ":" + e.message
                ex
            }
        }
    }
}

class ApiException(throwable: Throwable?, var code: Int) : Exception(throwable) {
    override var message: String? = null
}