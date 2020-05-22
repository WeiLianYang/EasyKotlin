package com.william.base_component.net

import com.william.base_component.BuildConfig
import com.william.base_component.net.interceptor.HeaderConfigInterceptor
import com.william.base_component.utils.ReleaseControl
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
object RetrofitHelper {

    private const val TIMEOUT: Long = 15

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .client(getOkHttpClient())
            .baseUrl(ReleaseControl.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
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

    fun <T> create(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }

}