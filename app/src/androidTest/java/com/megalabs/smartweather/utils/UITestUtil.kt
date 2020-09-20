package com.megalabs.smartweather.utils

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import timber.log.Timber


class UITestUtil {
    companion object {
        fun sleep(timeInMillis: Long) {
            try {
                Timber.d("Sleeping for %d", timeInMillis)
                Thread.sleep(timeInMillis)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        fun withViewAtPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
            return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
                override fun describeTo(description: Description?) {
                    itemMatcher.describeTo(description)
                }

                override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                    val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                    return viewHolder != null && itemMatcher.matches(viewHolder.itemView)
                }
            }
        }

        fun typeSearchViewText(text: String): ViewAction {
            return object : ViewAction {
                override fun getDescription(): String {
                    return "Change view text"
                }

                override fun getConstraints(): Matcher<View> {
                    return Matchers.allOf(
                        ViewMatchers.isDisplayed(),
                        ViewMatchers.isAssignableFrom(SearchView::class.java)
                    )
                }

                override fun perform(uiController: UiController?, view: View?) {
                    (view as SearchView).setQuery(text, false)
                }
            }
        }
    }
}