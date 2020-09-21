package com.megalabs.smartweather.extension

import com.megalabs.smartweather.exception.MaxRetriesExceededException
import com.megalabs.smartweather.exception.NetworkException
import com.megalabs.smartweather.utils.NetworkRetryHandler
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

fun <T> Single<T>.with(): Single<T> = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.with(schedulerProvider: SchedulerProvider): Single<T> =
    observeOn(schedulerProvider.ui()).subscribeOn(schedulerProvider.io())

fun <T,R> Single<T>.retryWithCount(count: Int, action: (Throwable) -> Flowable<R>): Single<T> =
    this.retryWhen { errors ->
        errors.countItems().flatMap { item ->
            if (item.second <= count) {
                action.invoke(item.first)
            } else {
                Flowable.error<T>(MaxRetriesExceededException(item.first))
            }
        }
    }

fun <T> Flowable<T>.countItems(): Flowable<Pair<T, Int>> =
    Flowable.zip(
        this,
        Flowable.range(1, Int.MAX_VALUE),
        BiFunction { item, count -> Pair(item, count) }
    )

fun <T> Single<T>.retryWhenNoInternet(): Single<T> =
    this.retryWhen {errors ->
        errors.flatMap {error ->
            if (error is NetworkException || error is MaxRetriesExceededException) {
                NetworkRetryHandler().askNetworkingAvailable(error)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .flatMap {
                        Flowable.just(it)
                    }
            } else {
                Flowable.error(error)
            }
        }
    }

fun <T> Single<T>.retryApiWithCount(count: Int): Single<T> =
    this.retryWithCount(count) { error ->
        if (error is NetworkException) {
            Flowable.just("retry").delay(500, TimeUnit.MILLISECONDS)
        }
        else {
            Flowable.error(error)
        }
    }.with().retryWhenNoInternet()