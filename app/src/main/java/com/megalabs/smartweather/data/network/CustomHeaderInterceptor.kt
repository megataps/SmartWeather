package com.megalabs.smartweather.data.network

import okhttp3.Interceptor
import okhttp3.Response

class CustomHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().newBuilder().apply {
            header("Content-Type", "application/json")
        }.build())
    }
}