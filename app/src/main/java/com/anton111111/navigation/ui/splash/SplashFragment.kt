package com.anton111111.navigation.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anton111111.navigation.R
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * If you do not need long work before start app do not use SplashFragment!
 */
class SplashFragment : Fragment(R.layout.splash_fragment) {

    private val viewModel: SplashViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isHasUser.collect {
                it?.run {
                    findNavController().navigate(
                        if (it)
                            SplashFragmentDirections.actionSplashToUserNavigation()
                        else
                            SplashFragmentDirections.actionSplashToGuestNavigation()
                    )
                }
            }
        }
    }

}