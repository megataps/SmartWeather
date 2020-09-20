package com.megalabs.smartweather.integration

import android.app.Application
import com.megalabs.smartweather.app.Constants
import com.megalabs.smartweather.data.repository.SearchRepository
import com.megalabs.smartweather.di.configureTestAppComponent
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mockito
import retrofit2.HttpException

// This is a kind of integration testing
class SearchRepositoryTest: KoinTest {
    private val mockedAndroidContext = Mockito.mock(Application::class.java)
    private val repository by inject<SearchRepository>()
    private val location = "saigon"
    private var searchCriteriaMap: MutableMap<String, String> = mutableMapOf()

    private fun initSearchCriteria() {
        searchCriteriaMap["q"] = location
        searchCriteriaMap["cnt"] = "10"
        searchCriteriaMap["appid"] = Constants.getApiAppId()
    }

    @Before
    fun before() {
        startKoin {
            androidContext(mockedAndroidContext)
            modules(configureTestAppComponent(Constants.getApiUrl()))
        }

        initSearchCriteria()
    }

    @After
    fun after() {
        stopKoin()
        searchCriteriaMap.clear()
    }

    @Test
    fun testGetWeatherSuccess() {
        repository.getDailyForecast(searchCriteriaMap).blockingGet()
        val test = repository.getDailyForecast(searchCriteriaMap).test()
        test.awaitCount(100)
        test.assertComplete()
    }

    @Test
    fun testGetWeatherFailed() {
        searchCriteriaMap["q"] = "aaa"
        searchCriteriaMap["cnt"] = "10"
        searchCriteriaMap["appid"] = Constants.getApiAppId()

        val test = repository.getDailyForecast(searchCriteriaMap).test()
        test.awaitCount(100)
        test.assertFailure(HttpException::class.java)
    }

    @Test
    fun testGetWeatherFailedByAppId() {
        searchCriteriaMap["q"] = "hanoi"
        searchCriteriaMap["cnt"] = "10"
        searchCriteriaMap["appid"] = "123"

        val test = repository.getDailyForecast(searchCriteriaMap).test()
        test.awaitCount(100)
        test.assertFailure(HttpException::class.java)
    }

    @Test
    fun testGetWeatherFailedByMaxResult() {
        searchCriteriaMap["q"] = "hanoi"
        searchCriteriaMap["cnt"] = "20"
        searchCriteriaMap["appid"] = Constants.getApiAppId()

        val test = repository.getDailyForecast(searchCriteriaMap).test()
        test.awaitCount(100)
        test.assertFailure(HttpException::class.java)
    }
}