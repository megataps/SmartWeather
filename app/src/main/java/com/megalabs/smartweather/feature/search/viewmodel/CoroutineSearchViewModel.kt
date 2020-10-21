package com.megalabs.smartweather.feature.search.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import arrow.core.Either
import com.megalabs.smartweather.feature.base.BaseViewModel
import com.megalabs.smartweather.feature.search.interactor.CoroutineSearchInteractor
import com.megalabs.smartweather.model.DailyForecast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class CoroutineSearchViewModel(
    context: Context,
    private val interactor: CoroutineSearchInteractor
): BaseViewModel(context) {

    private val viewModelJob = SupervisorJob()
    private val viewModeScope = CoroutineScope(Main + viewModelJob)

    val dailyForecastMutableLiveData by lazy { MutableLiveData<Either<Throwable, DailyForecast>>() }
    var dailyForecast: DailyForecast? = null

    fun dailyForecast(keyword: String) {
        Timber.e("LLLNNN>>>CoroutineSearchViewModel>>>dailyForecast")
        viewModeScope.launch {
            showLoading()
            try {
                val result = interactor.getDailyForecast(keyword)
                Timber.e("LLLNNN>>>result: $result")
                dailyForecastMutableLiveData.value = Either.right(result)

                Timber.e("LLLNNN>>>>CoroutineSearchViewModel>>>>${Thread.currentThread().name} has run.")
                hideLoading()
            } catch(throwable: Throwable) {
                Timber.e(throwable)
                dailyForecastMutableLiveData.value = Either.left(throwable)
            } finally {
                Timber.e("LLLNNN>>>CoroutineSearchViewModel>>>dailyForecast>>>finally")
                hideLoading()
            }
        }
    }

    override fun onCleared() {
        Timber.e("LLLNNN>>>CoroutineSearchViewModel>>>onCleared")
        super.onCleared()
        viewModelJob.cancel()
    }
}