package com.megalabs.smartweather.data.api

import com.megalabs.smartweather.model.DailyForecast
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface OpenWeatherMapApi {

    @GET("data/2.5/forecast/daily")
    fun getDailyForecast(
        @QueryMap apiSearchCriteria: Map<String, String>
    ): Single<DailyForecast>

}