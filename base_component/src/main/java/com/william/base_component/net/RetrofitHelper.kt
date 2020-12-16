package com.william.base_component.net

import com.william.base_component.BuildConfig
import com.william.base_component.net.interceptor.HeaderConfigInterceptor
import com.william.base_component.utils.apiBaseUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author William
 * @date 2020/5/19 11:43
 * Class Comment：
 */

private const val TIMEOUT: Long = 15

private fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
        .client(getOkHttpClient())
        .baseUrl(apiBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}

private fun getOkHttpClient(): OkHttpClient {
    val builder = OkHttpClient().newBuilder()
    val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    // TODO: William 2020/5/19 12:20 需要加上缓存、cookie、SSL证书设置
    builder.run {
        addInterceptor(httpLoggingInterceptor)
        addInterceptor(HeaderConfigInterceptor())
//            sslSocketFactory(sslSocketFactory, trustManager)
        connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        readTimeout(TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(TIMEOUT, TimeUnit.SECONDS)
    }
    return builder.build()
}

fun <T> createService(clazz: Class<T>): T {
    return getRetrofit().create(clazz)
}