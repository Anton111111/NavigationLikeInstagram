package com.anton111111.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.test.espresso.IdlingResource

class FragmentIdlingResource(
    private val fragmentManager: FragmentManager,
    private val fragmentClass: Class<out Fragment>
) : IdlingResource {
    private var callback: IdlingResource.ResourceCallback? = null

    override fun getName(): String = fragmentClass.name

    override fun isIdleNow(): Boolean {
        val rootNavHost = fragmentManager.findFragmentById(R.id.root_nav_host)
        val idle = rootNavHost?.run {
            rootNavHost?.childFragmentManager.fragments.firstOrNull {
                fragmentClass.isInstance(it)
            } == null
        } ?: true
        if (idle) {
            callback?.onTransitionToIdle()
        }
        return idle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }
}