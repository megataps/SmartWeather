package com.megalabs.smartweather

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.megalabs.smartweather.feature.MainActivity
import com.megalabs.smartweather.utils.UITestUtil.Companion.typeSearchViewText
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// TODO: Disable animation before running android test
// https://developer.android.com/training/testing/espresso/setup#set-up-environment
@RunWith(AndroidJUnit4::class)
@LargeTest
class DialogTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testLoadingDialogIsNotDisplayed() {
        Espresso.onView(withId(R.id.progressBar)).check { _, noViewFoundException ->
            assertEquals(true, noViewFoundException != null)
        }
    }

    @Test
    fun testShowLoadingWhenGetWeatherButtonTapped() {
        // When input to SearchView
        Espresso.onView(withId(R.id.searchView)).perform(typeSearchViewText("hanoi"))

        // Then
        Espresso.onView(withId(R.id.getWeatherButton)).check(matches(isEnabled()))

        // When perform click
        Espresso.onView(withId(R.id.getWeatherButton)).perform(ViewActions.click())

        // Then
        Espresso.onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
    }
}