package com.megalabs.smartweather.data.repository

import com.megalabs.smartweather.model.DailyForecast
import kotlinx.coroutines.Deferred

interface CoroutineSearchRepository {

    suspend fun getDailyForecastAsync(searchCriteriaMap: Map<String, String>): DailyForecast

}