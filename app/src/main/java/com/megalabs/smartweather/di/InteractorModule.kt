package com.megalabs.smartweather.di

import com.megalabs.smartweather.data.repository.CoroutineSearchRepository
import com.megalabs.smartweather.data.repository.SearchRepository
import com.megalabs.smartweather.feature.search.interactor.CoroutineSearchInteractor
import com.megalabs.smartweather.feature.search.interactor.CoroutineSearchInteractorImpl
import com.megalabs.smartweather.feature.search.interactor.SearchInteractor
import com.megalabs.smartweather.feature.search.interactor.SearchInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    // Search
    single { provideSearchInteractor(get()) }

    single { provideCoroutineSearchInteractor(get()) }
}

// Search
fun provideSearchInteractor(searchRepository: SearchRepository): SearchInteractor {
    return SearchInteractorImpl(searchRepository)
}

fun provideCoroutineSearchInteractor(searchRepository: CoroutineSearchRepository): CoroutineSearchInteractor {
    return CoroutineSearchInteractorImpl(searchRepository)
}