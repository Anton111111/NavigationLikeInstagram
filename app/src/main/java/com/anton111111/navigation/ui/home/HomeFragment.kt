package com.anton111111.navigation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anton111111.navigation.R
import com.anton111111.navigation.databinding.HomeFragmentBinding
import com.anton111111.navigation.ui.app.UserNavigationFragmentDirections

class HomeFragment : Fragment(R.layout.home_fragment) {

    private val viewBinding: HomeFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            paymentBtn.setOnClickListener {
                findNavController()
                Navigation.findNavController(requireActivity(), R.id.root_nav_host)
                    .navigate(UserNavigationFragmentDirections.actionUserToPayment())
            }

            toSecondScreenBtn.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeToSecond())
            }
        }
    }

}