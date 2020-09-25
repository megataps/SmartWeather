package com.megalabs.smartweather.data.api

import com.megalabs.smartweather.model.DailyForecast
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface CoroutineOpenWeatherMapApi {

    @GET("data/2.5/forecast/daily")
    suspend fun getDailyForecastAsync(
        @QueryMap apiSearchCriteria: Map<String, String>
    ): DailyForecast
}