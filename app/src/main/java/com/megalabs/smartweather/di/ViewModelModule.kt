package com.megalabs.smartweather.di

import com.megalabs.smartweather.extension.ApplicationSchedulerProvider
import com.megalabs.smartweather.extension.SchedulerProvider
import com.megalabs.smartweather.feature.search.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    // Search screen
    viewModel { SearchViewModel(get(), get(), get()) }

    single {
        provideSchedulerProvider()
    }
}

fun provideSchedulerProvider(): SchedulerProvider {
    return ApplicationSchedulerProvider()
}