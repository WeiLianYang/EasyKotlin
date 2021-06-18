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

package com.william.base_component.net.interceptor

import com.android.debugtools.bean.ApiLogBean
import com.android.debugtools.helper.ApiLogHelper
import com.william.base_component.Constants
import com.william.base_component.utils.IS_RELEASE
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

/**
 * @author William
 * @date 2020/5/19 11:31
 * Class Comment：
 */
class HeaderConfigInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        builder.addHeader("Authorization", Constants.token ?: "")
        builder.addHeader("os", "android")
        builder.addHeader("version", Constants.version)

        val response = chain.proceed(builder.build())
        collectApiLogData(request, response)
        return response
    }

    /**
     * 收集接口数据
     *
     * @param request request
     */
    @Throws(IOException::class)
    private fun collectApiLogData(
        request: Request,
        response: Response
    ) {
        if (!IS_RELEASE) {
            val utf8 = StandardCharsets.UTF_8
            val requestBody = request.body
            var body: String? = null
            if (requestBody != null) {
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                var charset = utf8
                val contentType = requestBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(utf8)
                }
                if (charset != null) {
                    body = buffer.readString(charset)
                }
            }
            val startNs = System.nanoTime()
            val requestTime = System.currentTimeMillis()
            val duration =
                TimeUnit.NANOSECONDS.toMillis(System.currentTimeMillis() - startNs)
            val responseBody = response.body
            if (responseBody != null) {
                val source = responseBody.source()
                source.request(Long.MAX_VALUE)
                val buffer = source.buffer
                val rBody = buffer.clone().readString(utf8)
                val headers = request.headers
                val headerJson = JSONObject()
                val iterator =
                    headers.iterator()
                while (iterator.hasNext()) {
                    val (first, second) = iterator.next()
                    try {
                        headerJson.put(first, second)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                val apiLogBean = ApiLogBean(
                    request.url.toString(), request.method,
                    headerJson.toString(), body, rBody, requestTime, duration
                )
                ApiLogHelper.instance.addApiLog(apiLogBean)
            }
        }
    }
}