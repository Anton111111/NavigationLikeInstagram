package com.anton111111.navigation

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.anton111111.navigation.ui.app.AppActivity
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    var activityTestRule = ActivityScenarioRule(AppActivity::class.java)

    @Test
    fun bottomNavTest_allItemsClick() {
        loginIsNeed()
        clickOnBottomItem(R.string.bottom_nav_search)
        isDisplayedAssertion(R.string.search_text)

        clickOnBottomItem(R.string.bottom_nav_favorites)
        isDisplayedAssertion(R.string.favorites_text)

        clickOnBottomItem(R.string.bottom_nav_profile)
        isDisplayedAssertion(R.string.profile_text)

        clickOnBottomItem(R.string.bottom_nav_home)
        isDisplayedAssertion(R.string.home_text)
    }

    @Test
    fun bottomNavTest_checkSaveState() {
        loginIsNeed()
        clickOnView(R.id.to_second_screen_btn)
        clickOnBottomItem(R.string.bottom_nav_search)
        clickOnView(R.id.to_second_screen_btn)
        clickOnBottomItem(R.string.bottom_nav_home)
        isDisplayedAssertion(R.string.home_second_text)
        clickOnBottomItem(R.string.bottom_nav_search)
        isDisplayedAssertion(R.string.search_second_text)
        clickOnBottomItem(R.string.bottom_nav_favorites)
        pressBack()
        isDisplayedAssertion(R.string.search_second_text)
        pressBack()
        isDisplayedAssertion(R.string.search_text)
        pressBack()
        isDisplayedAssertion(R.string.home_second_text)
        pressBack()
        isDisplayedAssertion(R.string.home_text)
    }

    private fun loginIsNeed() {
        try {
            clickOnView(R.id.login_btn)
        } catch (ignore: Exception) {
        }
    }

    private fun clickOnView(@IdRes idRes: Int) {
        Espresso.onView(
            CoreMatchers.allOf(
                withId(idRes), isDisplayed()
            )
        ).perform(ViewActions.click())
    }

    private fun clickOnBottomItem(itemStrResId: Int) {
        Espresso.onView(
            CoreMatchers.allOf(withContentDescription(itemStrResId), isDisplayed())
        ).perform(ViewActions.click())
    }

    private fun isDisplayedAssertion(@IdRes idStrRes: Int) {
        Espresso.onView(withText(idStrRes))
            .check(ViewAssertions.matches(isDisplayed()))
    }
}