package com.anton111111.navigation

import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.anton111111.navigation.ui.app.AppActivity
import com.anton111111.navigation.ui.splash.SplashFragment
import junit.framework.Assert.fail
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class NavigationTest {

    private lateinit var activity: AppActivity
    private lateinit var decorView: View
    private val fragmentIdlingResources: MutableMap<Class<out Fragment>, IdlingResource> =
        mutableMapOf()

    @get:Rule
    var activityTestRule = ActivityScenarioRule(AppActivity::class.java)

    @Before
    fun setUp() {
        registerFragmentIdleResource(SplashFragment::class.java)
        activityTestRule.scenario.onActivity {
            decorView = it.getWindow().getDecorView()
            activity = it
        }
    }

    @After
    fun tearDown() {
        unregisterFragmentIdleResource(SplashFragment::class.java)
    }

    private fun registerFragmentIdleResource(fragmentClass: Class<out Fragment>) {
        activityTestRule.scenario.onActivity {
            if (!fragmentIdlingResources.containsKey(fragmentClass))
                fragmentIdlingResources[fragmentClass] = FragmentIdlingResource(
                    it.supportFragmentManager,
                    fragmentClass,
                )
            IdlingRegistry.getInstance().register(fragmentIdlingResources[fragmentClass])
        }
    }

    private fun unregisterFragmentIdleResource(fragmentClass: Class<out Fragment>) {
        IdlingRegistry.getInstance().unregister(fragmentIdlingResources[fragmentClass])
    }

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

    @Test
    fun bottomNavTest_toPaymentFromHome() {
        loginIsNeed()
        clickOnView(R.id.payment_btn)
        isDisplayedAssertion(R.string.payment_text)
        pressBack()
        clickOnBottomItem(R.string.bottom_nav_home)
    }

    @Test
    fun bottomNavTest_toPaymentFromSearch() {
        loginIsNeed()
        clickOnBottomItem(R.string.bottom_nav_search)
        isDisplayedAssertion(R.string.search_text)
        clickOnView(R.id.to_payment_screen_btn)
        isDisplayedAssertion(R.string.payment_text)
        pressBack()
        isDisplayedAssertion(R.string.search_text)
        pressBack()
        isDisplayedAssertion(R.string.home_text)
        isEndBackStackAssertion()
    }

    /**
     * @TODO fix issue: Double back don't work in tests. And add Toast show checking
     */
    private fun isEndBackStackAssertion() {
        pressBackUnconditionally()
        if (!activity.isFinishing)
            fail("Activity is not closed. Seems back stack is wrong.")
    }

    private fun loginIsNeed() {
        try {
            clickOnView(R.id.login_btn)
        } catch (ignore: Exception) {
        }
    }

    private fun isToastDisplayed(@IdRes itemStrResId: Int) {
        onView(withText(itemStrResId)).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    private fun clickOnView(@IdRes idRes: Int) {
        onView(
            CoreMatchers.allOf(
                withId(idRes), isDisplayed()
            )
        ).perform(ViewActions.click())
    }

    private fun clickOnBottomItem(@IdRes itemStrResId: Int) {
        onView(
            CoreMatchers.allOf(withContentDescription(itemStrResId), isDisplayed())
        ).perform(ViewActions.click())
    }

    private fun isDisplayedAssertion(@IdRes idStrRes: Int) {
        onView(withText(idStrRes))
            .check(matches(isDisplayed()))
    }
}