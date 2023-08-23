package com.william.easykt.di

import android.content.Context
import android.os.Build
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.decode.VideoFrameDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import com.william.easykt.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = OkHttpClient.Builder()
//        .dispatcher(Dispatcher().apply { maxRequestsPerHost = maxRequests })
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                },
        )
        .build()

    /**
     * [Coil Gif](https://coil-kt.github.io/coil/gifs/)
     *
     * [Coil Svg](https://coil-kt.github.io/coil/svgs/)
     *
     * [Coil Video](https://coil-kt.github.io/coil/videos/)
     */
    @Provides
    @Singleton
    fun imageLoader(
        okHttpCallFactory: Call.Factory,
        @ApplicationContext application: Context,
    ): ImageLoader = ImageLoader.Builder(application)
        .components {
            // GIFs
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
            // SVGs
            add(SvgDecoder.Factory())
            // Video frames
            add(VideoFrameDecoder.Factory())
        }
        .memoryCache {
            MemoryCache.Builder(application)
                // Set the max size to 25% of the app's available memory.
                .maxSizePercent(0.25)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(application.filesDir.resolve("coil_image_cache"))
                .maxSizeBytes(512L * 1024 * 1024) // 512MB
                .build()
        }
        .callFactory(okHttpCallFactory)
//        .okHttpClient {
//            // Don't limit concurrent network requests by host.
//            val dispatcher = Dispatcher().apply { maxRequestsPerHost = maxRequests }
//
//            // Lazily create the OkHttpClient that is used for network operations.
//            OkHttpClient.Builder()
//                .dispatcher(dispatcher)
//                .build()
//        }
        // Show a short crossfade when loading images asynchronously.
        .crossfade(true)
        // Ignore the network cache headers and always read from/write to the disk cache.
        .respectCacheHeaders(false)
        // Enable logging to the standard Android log if this is a debug build.
        .apply { if (BuildConfig.DEBUG) logger(DebugLogger()) }
        .build()
}
