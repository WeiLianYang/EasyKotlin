package com.william.base_component.net.exception

/**
 * @author William
 * @date 2020/5/22 12:04
 * Class Comment：
 */
object NetCode {
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
}