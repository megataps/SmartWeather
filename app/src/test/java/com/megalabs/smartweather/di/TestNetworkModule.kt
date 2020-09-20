package com.megalabs.smartweather.di

import com.megalabs.smartweather.cache.TestCacheInterceptor
import com.megalabs.smartweather.data.network.CustomHeaderInterceptor
import com.squareup.moshi.Moshi
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

const val CONNECT_TIMEOUT = 30L
const val WRITE_TIMEOUT = 30L
const val READ_TIMEOUT = 30L
const val CACHE_MAX_SIZE = 50L * 1024 * 1024 // 50 MB

// This is the a trick for unit testing
// Because of mock application does not have cache directory
private const val DEFAULT_CACHE_PATH = "/data/user/0/com.megalabs.smartweather.develop/cache"

fun configureNetworkModuleForTest(baseApi: String) = module {

    single {
        Cache(
            androidApplication().cacheDir ?: File(DEFAULT_CACHE_PATH),
            CACHE_MAX_SIZE)
    }

    single {
        Moshi.Builder()
            .build()
    }

    single {
        CustomHeaderInterceptor()
    }

    single {
        TestCacheInterceptor(get())
    }

    single {
        OkHttpClient.Builder().apply {
            cache(get())
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(get<CustomHeaderInterceptor>())
            addInterceptor(get<TestCacheInterceptor>())
        }.build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(baseApi)
            .client(get())
            .addCallAdapterFactory(
                RxJava3CallAdapterFactory.createWithScheduler(
                Schedulers.io()))
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }
}