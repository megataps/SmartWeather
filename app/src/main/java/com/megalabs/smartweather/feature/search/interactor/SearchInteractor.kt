package com.megalabs.smartweather.feature.search.interactor

import com.megalabs.smartweather.model.DailyForecast
import io.reactivex.rxjava3.core.Single

interface SearchInteractor {

    fun getDailyForecast(keyword: String): Single<DailyForecast>
}