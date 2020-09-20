package com.megalabs.smartweather

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.megalabs.smartweather.assertion.RecyclerViewItemCountAssertion
import com.megalabs.smartweather.feature.MainActivity
import com.megalabs.smartweather.utils.UITestUtil
import com.megalabs.smartweather.utils.UITestUtil.Companion.typeSearchViewText
import org.hamcrest.core.IsNot.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


// TODO: Disable animation before running android test
// https://developer.android.com/training/testing/espresso/setup#set-up-environment
@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testGetWeatherButtonIsDisplayed() {
        Espresso.onView(withId(R.id.getWeatherButton)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchViewIsDisplayed() {
        Espresso.onView(withId(R.id.searchView)).check(matches(isDisplayed()))
    }

    @Test
    fun testNotFoundViewIsNotDisplayed() {
        Espresso.onView(withId(R.id.emptyContainerView)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testGetWeatherButtonIsDisabled() {
        Espresso.onView(withId(R.id.getWeatherButton)).check(matches(not(isEnabled())))
    }

    @Test
    fun testGetWeatherButtonIsEnabled() {
        Espresso.onView(withId(R.id.searchView)).perform(typeSearchViewText("123"))
        Espresso.onView(withId(R.id.getWeatherButton)).check(matches(isEnabled()))
    }

    @Test
    fun testGetWeatherButtonIsDisabledWhenInputSearchView() {
        Espresso.onView(withId(R.id.searchView)).perform(typeSearchViewText("12"))
        Espresso.onView(withId(R.id.getWeatherButton)).check(matches(not(isEnabled())))
    }

    @Test
    fun testShowNotFoundView() {
        // When
        Espresso.onView(withId(R.id.searchView)).perform(typeSearchViewText("aaa"))
        Espresso.onView(withId(R.id.getWeatherButton)).perform(ViewActions.click())

        // Then
        UITestUtil.sleep(2 * 1000) // Delay 2 seconds
        Espresso.onView(withId(R.id.emptyContainerView)).check(matches(isDisplayed()))
    }

    @Test
    fun testDisplayList() {
        // When
        Espresso.onView(withId(R.id.searchView)).perform(typeSearchViewText("hanoi"))
        Espresso.onView(withId(R.id.getWeatherButton)).perform(ViewActions.click())

        // Then
        UITestUtil.sleep(2 * 1000) // Delay 2 seconds
        Espresso.onView(withId(R.id.searchRecyclerView)).check(RecyclerViewItemCountAssertion(16))
    }
}