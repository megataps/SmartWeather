package com.megalabs.smartweather.app

import com.megalabs.smartweather.BuildConfig

object Constants {

    const val MAX_RETRY_COUNT = 1

    fun isDebug(): Boolean {
        return BuildConfig.IS_BUILD_DEBUG
    }

    fun getApiUrl(): String {
        return BuildConfig.API_ENDPOINT
    }

    fun getApiAppId(): String {
        return BuildConfig.API_APP_ID
    }
}