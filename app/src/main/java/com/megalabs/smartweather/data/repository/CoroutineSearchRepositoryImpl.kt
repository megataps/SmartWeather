package com.megalabs.smartweather.data.repository

import com.megalabs.smartweather.data.api.CoroutineOpenWeatherMapApi
import com.megalabs.smartweather.model.DailyForecast
import kotlinx.coroutines.Deferred

class CoroutineSearchRepositoryImpl(
    private val openWeatherMapApi: CoroutineOpenWeatherMapApi
): CoroutineSearchRepository {

    override suspend fun getDailyForecastAsync(searchCriteriaMap: Map<String, String>): DailyForecast {
        return openWeatherMapApi.getDailyForecastAsync(searchCriteriaMap)
    }
}