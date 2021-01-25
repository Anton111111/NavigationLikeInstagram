/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anton111111.navigation

import android.content.Intent
import android.util.Log
import android.util.SparseArray
import androidx.core.util.containsKey
import androidx.core.util.set
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Manages the various graphs needed for a [BottomNavigationView].
 * Got from https://github.com/android/architecture-components-samples/tree/master/NavigationAdvancedSample
 * and reworked
 */
/**
 * @TODO Since the first fragment does not have a tag by default, it always remains at the beginning of the back stack. Need to find a solution so that the first fragment leaves the back stack as soon as it enters the back stack with the added tag. For example, for path A(the first)->B->C->A back stack should be as follows <-C<-B<-exit
 */
fun BottomNavigationView.setupWithNavController(
    navGraphIds: List<Int>,
    fragmentManager: FragmentManager,
    containerId: Int,
    intent: Intent
): LiveData<NavController> {
    // Map of tags
    val graphIdToTagMap = SparseArray<String>()
    // Result. Mutable live data with the selected controlled
    val selectedNavController = MutableLiveData<NavController>()

    var firstFragmentGraphId = 0

    // First create a NavHostFragment for each NavGraph ID
    navGraphIds.forEachIndexed { index, navGraphId ->
        val fragmentTag = getFragmentTag(index)

        // Find or create the Navigation host fragment
        val navHostFragment = obtainNavHostFragment(
            fragmentManager,
            fragmentTag,
            navGraphId,
            containerId
        )

        // Obtain its id
        val graphId = navHostFragment.navController.graph.id

        if (index == 0) {
            firstFragmentGraphId = graphId
        }

        // Save to the map
        graphIdToTagMap[graphId] = fragmentTag

        // Attach or detach nav host fragment depending on whether it's the selected item.
        if (this.selectedItemId == graphId) {
            // Update livedata with the selected graph
            selectedNavController.value = navHostFragment.navController
            attachNavHostFragment(fragmentManager, navHostFragment, index == 0)
        } else {
            detachNavHostFragment(fragmentManager, navHostFragment)
        }
    }

    // Now connect selecting an item with swapping Fragments
    var selectedItemTag = graphIdToTagMap[this.selectedItemId]
    val firstFragmentTag = graphIdToTagMap[firstFragmentGraphId]

    // When a navigation item is selected
    setOnNavigationItemSelectedListener { item ->
        // Don't do anything if the state is state has already been saved.
        if (fragmentManager.isStateSaved) {
            false
        } else {
            val newlySelectedItemTag = graphIdToTagMap[item.itemId]
            if (selectedItemTag != newlySelectedItemTag) {
                Log.e("!!!!", "!!!!!$selectedItemTag != $newlySelectedItemTag")
                //Check if fragment already shown
                fragmentManager.currentNavHostFragmentTag(graphIdToTagMap)?.run {
                    if (this == newlySelectedItemTag) {
                        selectedItemTag = newlySelectedItemTag
                        fragmentManager.currentNavHostFragment?.run {
                            selectedNavController.value = navController
                        }
                        return@setOnNavigationItemSelectedListener true
                    }
                }

                val newlySelectedFragment =
                    fragmentManager.findFragmentByTag(newlySelectedItemTag)
                            as NavHostFragment

                //Check if we need pop back stack ot add to back stack.
                when {
                    //If the newly already on back stack or is first fragment then clear back stack
                    //upper the newly (included the newly) and restore back stack with the newly on up.
                    fragmentManager.isOnBackStack(newlySelectedItemTag) || newlySelectedItemTag == firstFragmentTag -> {
                        val tagsForRestoreBackstack: MutableList<String> = mutableListOf()
                        var tagForDetach: String? = firstFragmentTag
                        for (i in fragmentManager.backStackEntryCount - 1 downTo 0) {
                            val backStackEntry = fragmentManager.getBackStackEntryAt(i)
                            if (backStackEntry.name == newlySelectedItemTag) {
                                if (i > 0)
                                    tagForDetach = fragmentManager.getBackStackEntryAt(i - 1).name
                                break
                            }
                            backStackEntry.name?.let { tagsForRestoreBackstack.add(it) }
                        }
                        tagsForRestoreBackstack.reverse()
                        tagsForRestoreBackstack.add(newlySelectedItemTag)
                        val backToTag = when {
                            fragmentManager.isOnBackStack(newlySelectedItemTag) -> newlySelectedItemTag
                            fragmentManager.backStackEntryCount > 0 ->
                                fragmentManager.getBackStackEntryAt(0).name
                            else -> null
                        }
                        fragmentManager.popBackStack(backToTag, POP_BACK_STACK_INCLUSIVE)
                        tagsForRestoreBackstack.forEach { tag ->
                            val forAttach = fragmentManager.findFragmentByTag(tag)!!
                            fragmentManager.beginTransaction()
                                .attach(forAttach)
                                .setPrimaryNavigationFragment(forAttach)
                                .apply {
                                    if (tagForDetach != tag)
                                        detach(fragmentManager.findFragmentByTag(tagForDetach)!!)
                                }
                                //Do not add to back stack first fragment because it always here
                                .apply { if (tag != firstFragmentTag) addToBackStack(tag) }
                                .setReorderingAllowed(true)
                                .commit()
                            tagForDetach = tag
                        }
                    }
                    //Else add to back stack
                    else -> {
                        fragmentManager.beginTransaction()
                            .attach(newlySelectedFragment)
                            .setPrimaryNavigationFragment(newlySelectedFragment)
                            .detach(fragmentManager.findFragmentByTag(selectedItemTag)!!)
                            .addToBackStack(newlySelectedItemTag)
                            .setReorderingAllowed(true)
                            .commit()
                    }
                }

                selectedItemTag = newlySelectedItemTag
                selectedNavController.value = newlySelectedFragment.navController
                true
            } else {
                false
            }
        }
    }

    // Optional: on item reselected, pop back stack to the destination of the graph
    setupItemReselected(graphIdToTagMap, fragmentManager)

    // Handle deep link
    setupDeepLinks(navGraphIds, fragmentManager, containerId, intent)

    // Finally, ensure that we update our BottomNavigationView when the back stack changes
    fragmentManager.addOnBackStackChangedListener {
        fragmentManager.currentNavHostFragment?.run {
            if (selectedItemId != navController.graph.id)
                selectedItemId = navController.graph.id
        }
        // Reset the graph if the currentDestination is not valid (happens when the back
        // stack is popped after using the back button).
        selectedNavController.value?.let { controller ->
            if (controller.currentDestination == null) {
                controller.navigate(controller.graph.id)
            }
        }
    }
    return selectedNavController
}

private val FragmentManager.currentNavHostFragment: NavHostFragment?
    get() = fragments.findLast {
        it is NavHostFragment && !it.tag.isNullOrBlank()
    }?.let { it as NavHostFragment }

private fun FragmentManager.currentNavHostFragmentTag(graphIdToTagMap: SparseArray<String>): String? =
    currentNavHostFragment?.let {
        if (graphIdToTagMap.containsKey(it.navController.graph.id))
            graphIdToTagMap[it.navController.graph.id] else null
    }


private fun BottomNavigationView.setupDeepLinks(
    navGraphIds: List<Int>,
    fragmentManager: FragmentManager,
    containerId: Int,
    intent: Intent
) {
    navGraphIds.forEachIndexed { index, navGraphId ->
        val fragmentTag = getFragmentTag(index)

        // Find or create the Navigation host fragment
        val navHostFragment = obtainNavHostFragment(
            fragmentManager,
            fragmentTag,
            navGraphId,
            containerId
        )
        // Handle Intent
        if (navHostFragment.navController.handleDeepLink(intent)
            && selectedItemId != navHostFragment.navController.graph.id
        ) {
            this.selectedItemId = navHostFragment.navController.graph.id
        }
    }
}

private fun BottomNavigationView.setupItemReselected(
    graphIdToTagMap: SparseArray<String>,
    fragmentManager: FragmentManager
) {
    setOnNavigationItemReselectedListener { item ->
        val newlySelectedItemTag = graphIdToTagMap[item.itemId]
        val selectedFragment = fragmentManager.findFragmentByTag(newlySelectedItemTag)
                as NavHostFragment
        val navController = selectedFragment.navController
        // Pop the back stack to the start destination of the current navController graph
        navController.popBackStack(
            navController.graph.startDestination, false
        )
    }
}

private fun detachNavHostFragment(
    fragmentManager: FragmentManager,
    navHostFragment: NavHostFragment
) {
    fragmentManager.beginTransaction()
        .detach(navHostFragment)
        .commitNow()
}

private fun attachNavHostFragment(
    fragmentManager: FragmentManager,
    navHostFragment: NavHostFragment,
    isPrimaryNavFragment: Boolean
) {
    fragmentManager.beginTransaction()
        .attach(navHostFragment)
        .apply {
            if (isPrimaryNavFragment)
                setPrimaryNavigationFragment(navHostFragment)
        }
        .commitNow()
}

private fun obtainNavHostFragment(
    fragmentManager: FragmentManager,
    fragmentTag: String,
    navGraphId: Int,
    containerId: Int
): NavHostFragment {
    // If the Nav Host fragment exists, return it
    val existingFragment = fragmentManager.findFragmentByTag(fragmentTag) as NavHostFragment?
    existingFragment?.let { return it }

    // Otherwise, create it and return it.
    val navHostFragment = NavHostFragment.create(navGraphId)
    fragmentManager.beginTransaction()
        .add(containerId, navHostFragment, fragmentTag)
        .commitNow()
    return navHostFragment
}

private fun FragmentManager.isOnBackStack(backStackName: String): Boolean {
    val backStackCount = backStackEntryCount
    for (index in 0 until backStackCount) {
        if (getBackStackEntryAt(index).name == backStackName) {
            return true
        }
    }
    return false
}

private fun getFragmentTag(index: Int) = "bottomNavigation#$index"
