package com.megalabs.smartweather.feature.search.interactor

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.megalabs.smartweather.app.Constants
import com.megalabs.smartweather.data.repository.SearchRepository
import com.megalabs.smartweather.di.configureTestAppComponent
import com.megalabs.smartweather.feature.BaseUnitTest
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mockito
import java.net.HttpURLConnection

class SearchInteractorTest: KoinTest, BaseUnitTest() {

    private val mockedAndroidContext = Mockito.mock(Application::class.java)
    private val searchRepository by inject<SearchRepository>()

    private var searchCriteriaMap: MutableMap<String, String> = mutableMapOf()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    override fun before() {
        super.before()
        startKoin {
            androidContext(mockedAndroidContext)
            modules(configureTestAppComponent(mockServer.url("/").toString()))
        }

        searchCriteriaMap["q"] = "hanoi"
        searchCriteriaMap["cnt"] = "16"
        searchCriteriaMap["appid"] = Constants.getApiAppId()
    }

    @After
    override fun after() {
        super.after()
        stopKoin()
        searchCriteriaMap.clear()
    }

    @Test
    fun testGetDailyForecastSuccess() {
        mockServer.enqueue(
            mockResponseFromFile(
                fileName = "dailyforecast_response.json",
                responseCode = HttpURLConnection.HTTP_OK
            )
        )

        val result = searchRepository.getDailyForecast(searchCriteriaMap).blockingGet()
        assertEquals("Loading should be successful", 16, result.list.size)
    }
}