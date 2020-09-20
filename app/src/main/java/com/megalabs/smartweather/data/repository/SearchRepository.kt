package com.megalabs.smartweather.data.repository

import com.megalabs.smartweather.model.DailyForecast
import io.reactivex.rxjava3.core.Single

interface SearchRepository {

    fun getDailyForecast(searchCriteriaMap: Map<String, String>): Single<DailyForecast>

}