package com.megalabs.smartweather.data.repository

import com.megalabs.smartweather.data.api.OpenWeatherMapApi
import com.megalabs.smartweather.model.DailyForecast
import io.reactivex.rxjava3.core.Single

class SearchRepositoryImpl(
    private val openWeatherMapApi: OpenWeatherMapApi
): SearchRepository {

    override fun getDailyForecast(searchCriteriaMap: Map<String, String>): Single<DailyForecast> {
        return openWeatherMapApi.getDailyForecast(searchCriteriaMap)
    }
}