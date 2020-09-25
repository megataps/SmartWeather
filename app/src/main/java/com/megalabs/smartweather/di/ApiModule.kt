package com.megalabs.smartweather.di

import com.megalabs.smartweather.data.api.CoroutineOpenWeatherMapApi
import com.megalabs.smartweather.data.api.OpenWeatherMapApi
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {

    single(createdAtStart = false){ get<Retrofit>().create(OpenWeatherMapApi::class.java) }

    single(createdAtStart = false){ get<Retrofit>().create(CoroutineOpenWeatherMapApi::class.java) }

}