package com.megalabs.smartweather.di

import com.megalabs.smartweather.data.api.OpenWeatherMapApi
import com.megalabs.smartweather.data.repository.SearchRepository
import com.megalabs.smartweather.data.repository.SearchRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    // Search
    single { provideSearchRepository(get()) }

}

// Search
fun provideSearchRepository(openWeatherMapApi: OpenWeatherMapApi): SearchRepository {
    return SearchRepositoryImpl(openWeatherMapApi)
}