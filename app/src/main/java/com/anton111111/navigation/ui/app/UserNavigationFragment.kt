package com.anton111111.navigation.ui.app

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anton111111.navigation.R
import com.anton111111.navigation.databinding.UserNavigationFragmentBinding
import com.anton111111.navigation.setupWithNavController
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class UserNavigationFragment : Fragment(R.layout.user_navigation_fragment) {

    private val viewBinding: UserNavigationFragmentBinding by viewBinding()
    private lateinit var currentNavController: StateFlow<NavController?>
    private val bottomNavViewModel: BottomNavigationSharedViewModel by sharedViewModel()


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null)
            setupBottomNavigationBar()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null)
            setupBottomNavigationBar()

        observeNavActions()
    }

    private fun observeNavActions() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            bottomNavViewModel.bottomNavActionSharedState.collect { action ->
                with(viewBinding.bottomNav) {
                    if (selectedItemId != action.bottomNavItemId) {
                        selectedItemId = action.bottomNavItemId
                        //Wait until finish all fragment manager transactions
                        childFragmentManager.executePendingTransactions()
                    }
                    action.destinationId?.let { destId ->
                        currentNavController.value?.navigate(destId)
                    }
                }
            }
        }

    }

    private fun setupBottomNavigationBar() {
        //Call in launchWhenStarted to avoid case when bottom nav view is not restored yet
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            currentNavController = viewBinding.bottomNav.setupWithNavController(
                navGraphIds = listOf(
                    Pair(R.id.home_navigation, R.navigation.home_navigation),
                    Pair(R.id.search_navigation, R.navigation.search_navigation),
                    Pair(R.id.favorites_navigation, R.navigation.favorites_navigation),
                    Pair(R.id.profile_navigation, R.navigation.profile_navigation)
                ),
                fragmentManager = childFragmentManager,
                containerId = viewBinding.navHost.id,
                intent = requireActivity().intent
            )
        }
    }

}