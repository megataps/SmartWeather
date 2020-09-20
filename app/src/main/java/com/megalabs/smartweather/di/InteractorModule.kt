package com.megalabs.smartweather.di

import com.megalabs.smartweather.data.repository.SearchRepository
import com.megalabs.smartweather.feature.search.interactor.SearchInteractor
import com.megalabs.smartweather.feature.search.interactor.SearchInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    // Search
    single { provideSearchInteractor(get()) }
}

// Search
fun provideSearchInteractor(searchRepository: SearchRepository): SearchInteractor {
    return SearchInteractorImpl(searchRepository)
}