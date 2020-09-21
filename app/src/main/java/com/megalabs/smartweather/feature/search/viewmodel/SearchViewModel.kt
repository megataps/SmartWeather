package com.megalabs.smartweather.feature.search.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import arrow.core.Either
import com.megalabs.smartweather.exception.NetworkException
import com.megalabs.smartweather.exception.NotFoundException
import com.megalabs.smartweather.extension.SchedulerProvider
import com.megalabs.smartweather.extension.with
import com.megalabs.smartweather.feature.base.BaseViewModel
import com.megalabs.smartweather.feature.search.interactor.SearchInteractor
import com.megalabs.smartweather.model.DailyForecast
import io.reactivex.rxjava3.kotlin.subscribeBy
import timber.log.Timber

class SearchViewModel(
    context: Context,
    private val interactor: SearchInteractor,
    private val schedulerProvider: SchedulerProvider
): BaseViewModel(context) {

    val dailyForecastMutableLiveData by lazy { MutableLiveData<Either<Throwable, DailyForecast>>() }
    var dailyForecast: DailyForecast? = null

    fun dailyForecast(keyword: String) {
        addToDisposable(interactor.getDailyForecast(keyword).with(schedulerProvider)
            .doOnError(Timber::e)
            .doOnSubscribe { showLoading() }
            .doOnEvent { _, _ -> hideLoading() }
            .doOnError { onErrorHandler(it)}
            .subscribeBy(
                onError = {
                    dailyForecastMutableLiveData.value = Either.left(it)
                },
                onSuccess = {
                    dailyForecast = it
                    dailyForecastMutableLiveData.value = Either.right(it)
                }
            ))
    }

    override fun onErrorHandler(error: Throwable) {
        if (error is NotFoundException || error is NetworkException) {
            // Show inline message instead of popup
        } else {
            super.onErrorHandler(error)
        }
    }
}