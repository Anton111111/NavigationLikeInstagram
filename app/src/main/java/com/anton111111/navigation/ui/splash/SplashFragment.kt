package com.anton111111.navigation.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anton111111.navigation.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : Fragment(R.layout.splash_fragment) {

    private val viewModel: SplashViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            findNavController().navigate(
                if (viewModel.isHasUser)
                    SplashFragmentDirections.actionSplashToUserNavigation()
                else
                    SplashFragmentDirections.actionSplashToGuestNavigation()
            )
        }
    }

}