package com.megalabs.smartweather

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ApplicationTest {

//    lateinit var instrumentationContext: Context

//    @Before()
//    fun before() {
//        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
//        startKoin {
//            androidLogger(Level.DEBUG)
//            androidContext(instrumentationContext)
//            modules(weatherAppModule)
//        }
//        loadKoinModules(weatherAppModule)
//    }

//    @After
//    fun after() {
//        stopKoin()
//    }

//    @Test
//    fun testLoadFragment() {
//        val scenario = launchFragmentInContainer<SearchFragment>()
//        scenario.recreate()
//        Espresso.onView(withId(R.id.getWeatherButton)).perform(ViewActions.click())
//    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.megalabs.smartweather.develop", appContext.packageName)
    }

}