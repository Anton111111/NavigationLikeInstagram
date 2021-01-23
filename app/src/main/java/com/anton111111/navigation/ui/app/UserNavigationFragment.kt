package com.anton111111.navigation.ui.app

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anton111111.navigation.R
import com.anton111111.navigation.databinding.UserNavigationFragmentBinding
import com.anton111111.navigation.setupWithNavController

class UserNavigationFragment : Fragment(R.layout.user_navigation_fragment) {

    private val viewBinding: UserNavigationFragmentBinding by viewBinding()
    private var currentNavController: LiveData<NavController>? = null

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null)
            setupBottomNavigationBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    private fun setupBottomNavigationBar() {
        currentNavController = viewBinding.bottomNav.setupWithNavController(
            navGraphIds = listOf(
                R.navigation.home_navigation,
                R.navigation.search_navigation,
                R.navigation.favorites_navigation,
                R.navigation.profile_navigation
            ),
            fragmentManager = childFragmentManager,
            containerId = viewBinding.bottomNavigationNavHost.id,
            intent = requireActivity().intent
        )
    }


}