package com.megalabs.smartweather.di

import android.app.Application
import com.megalabs.smartweather.app.weatherAppModule
import com.megalabs.smartweather.feature.search.viewmodel.SearchViewModel
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import org.mockito.Mockito.mock

class ModuleCheckTest : KoinTest {
    private val mockedAndroidContext = mock(Application::class.java)
    private val viewModelId = "someId"

    @Test
    fun testInitKoinConfiguration() {
        koinApplication {
            androidContext(mockedAndroidContext)
            modules(weatherAppModule)
        }.checkModules {
            create<SearchViewModel> { parametersOf(viewModelId) }
        }
    }
}

fun configureTestAppComponent(baseApi: String) = listOf(
    configureNetworkModuleForTest(baseApi),
    apiModule,
    repositoryModule,)