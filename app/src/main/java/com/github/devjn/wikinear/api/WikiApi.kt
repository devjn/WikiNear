package com.github.devjn.wikinear.api

import com.github.devjn.wikinear.App
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * Created by @author Jahongir on 26-Jan-19
 * devjn@jn-arts.com
 * WikiApi
 */
object WikiApi {

    private const val CACHE_CONTROL = "Cache-Control"
    private const val BASE_URL = "https://en.wikipedia.org/w/"

    private val okHttp: OkHttpClient

    init {
        val httpCacheDirectory = File(App.appContext.cacheDir, "responses")
        val cacheSize = 10L * 1024 * 1024 // 10 MiB
        val cache = Cache(httpCacheDirectory, cacheSize)

        okHttp = OkHttpClient.Builder()
                //.addInterceptor(provideOfflineCacheInterceptor())
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(cache)
                .build()
    }

    private val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttp)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

    private fun provideCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())

            // re-write response header to force use of cache
            val cacheControl = CacheControl.Builder()
                    .maxAge(10, TimeUnit.MINUTES)
                    .build()

            response.newBuilder()
                    .header(CACHE_CONTROL, cacheControl.toString())
                    .build()
        }
    }

    @JvmStatic
    fun changeApiBaseUrl(newApiBaseUrl: String) {
        builder.baseUrl(newApiBaseUrl)
    }


    fun <S> createService(serviceClass: Class<S>): S {
        val retrofit = builder.build()
        return retrofit.create(serviceClass)
    }

}