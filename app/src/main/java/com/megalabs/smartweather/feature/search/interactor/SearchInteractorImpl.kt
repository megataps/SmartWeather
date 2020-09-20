package com.megalabs.smartweather.feature.search.interactor

import com.megalabs.smartweather.app.Constants
import com.megalabs.smartweather.data.repository.SearchRepository
import com.megalabs.smartweather.extension.retryApiWithCount
import com.megalabs.smartweather.feature.base.BaseInteractor
import com.megalabs.smartweather.model.DailyForecast
import io.reactivex.rxjava3.core.Single
import timber.log.Timber

class SearchInteractorImpl(
    private val searchRepository: SearchRepository
): SearchInteractor, BaseInteractor() {

    override fun getDailyForecast(keyword: String): Single<DailyForecast> {
        Timber.e("LLLNNN>>>SearchInteractorImpl>>>>getDailyForecast")
        val searchCriteriaMap: MutableMap<String, String> = mutableMapOf()
        searchCriteriaMap["q"] = keyword
        // TODO: The maximum of result is just 17 items
        // cnt's range is [1-17]
        searchCriteriaMap["cnt"] = "16"
        searchCriteriaMap["appid"] = Constants.getApiAppId()

        return searchRepository.getDailyForecast(searchCriteriaMap)
            .onErrorResumeNext {
                Single.error(transformException(it))
            }
            .retryApiWithCount(Constants.MAX_RETRY_COUNT)
    }
}