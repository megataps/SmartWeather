package com.megalabs.smartweather.feature.search.interactor

import com.megalabs.smartweather.app.Constants
import com.megalabs.smartweather.data.repository.CoroutineSearchRepository
import com.megalabs.smartweather.feature.base.BaseInteractor
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import timber.log.Timber

class CoroutineSearchInteractorImpl(
    private val searchRepository: CoroutineSearchRepository
): CoroutineSearchInteractor, BaseInteractor() {

    override suspend fun getDailyForecast(keyword: String) = withContext(IO) {
        Timber.e("LLLNNN>>>CoroutineSearchInteractorImpl>>>getDailyForecast")
        val searchCriteriaMap: MutableMap<String, String> = mutableMapOf()
        searchCriteriaMap["q"] = keyword
        // TODO: The maximum of result is just 17 items
        // cnt's range is [1-17]
        searchCriteriaMap["cnt"] = "16"
        searchCriteriaMap["appid"] = Constants.getApiAppId()

        searchRepository.getDailyForecastAsync(searchCriteriaMap)
    }
}