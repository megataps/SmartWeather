package com.megalabs.smartweather.feature.search.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Left
import com.megalabs.smartweather.exception.ApiException
import com.megalabs.smartweather.exception.InternalServerException
import com.megalabs.smartweather.exception.NetworkException
import com.megalabs.smartweather.exception.NotFoundException
import com.megalabs.smartweather.feature.search.interactor.SearchInteractor
import com.megalabs.smartweather.mock.TestSchedulerProvider
import com.megalabs.smartweather.model.DailyForecast
import io.reactivex.rxjava3.core.Single
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SearchViewModelTest {

    val keyword: String = "hanoi"

    lateinit var viewModel: SearchViewModel

    private val mockedAndroidContext = Mockito.mock(Application::class.java)

    @Mock
    lateinit var searchInteractor: SearchInteractor

    @Mock
    lateinit var dailyForecast: DailyForecast

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        viewModel = SearchViewModel(mockedAndroidContext, searchInteractor, TestSchedulerProvider())
    }

    @After
    fun after() {

    }

    fun preCheckTestcase() {
        //Check Initial
        assertEquals(
            "Before perform test initial state should be null",
            null,
            this.viewModel.dailyForecastMutableLiveData.value,
        )
    }

    @Test
    fun testSearchSuccess() {

        preCheckTestcase()

        given(searchInteractor.getDailyForecast(keyword)).willReturn(Single.just(dailyForecast))
        viewModel.dailyForecast(keyword)
        val result = viewModel.dailyForecastMutableLiveData.value
        assertEquals("Search should be successful", true, result?.isRight())
    }

    @Test
    fun testSearchFailed() {

        preCheckTestcase()

        val error = Throwable("Got an error")
        given(searchInteractor.getDailyForecast(keyword)).willReturn(Single.error(error))

        viewModel.dailyForecast(keyword)

        val result = viewModel.dailyForecastMutableLiveData.value
        assertEquals("Search should be failed", true, result?.isLeft())
    }

    @Test
    fun testSearchWithNetworkError() {
        preCheckTestcase()

        val exception = NetworkException(Throwable("No Internet error"))
        given(searchInteractor.getDailyForecast(keyword)).willReturn(Single.error(exception))

        viewModel.dailyForecast(keyword)

        val result = viewModel.dailyForecastMutableLiveData.value
        assertEquals("Search should be failed", true, result?.isLeft())
        assertEquals("Error should be NetworkException", Left(exception), result)
    }

    @Test
    fun testSearchWithNotFoundException() {
        preCheckTestcase()

        val exception = NotFoundException("Item not found")
        given(searchInteractor.getDailyForecast(keyword)).willReturn(Single.error(exception))

        viewModel.dailyForecast(keyword)

        val result = viewModel.dailyForecastMutableLiveData.value
        assertEquals("Search should be failed", true, result?.isLeft())
        assertEquals("Error should be NotFoundException", Left(exception), result)
    }

    @Test
    fun testSearchWithInternalServerException() {
        preCheckTestcase()

        val exception = InternalServerException("Internal server exception")
        given(searchInteractor.getDailyForecast(keyword)).willReturn(Single.error(exception))

        viewModel.dailyForecast(keyword)

        val result = viewModel.dailyForecastMutableLiveData.value
        assertEquals("Search should be failed", true, result?.isLeft())
        assertEquals("Error should be InternalServerException", Left(exception), result)
    }

    @Test
    fun testSearchWithApiException() {
        preCheckTestcase()

        val exception = ApiException("400", "Invalid request")
        given(searchInteractor.getDailyForecast(keyword)).willReturn(Single.error(exception))

        viewModel.dailyForecast(keyword)

        val result = viewModel.dailyForecastMutableLiveData.value
        assertEquals("Search should be failed", true, result?.isLeft())
        assertEquals("Error should be ApiException", Left(exception), result)
    }
}