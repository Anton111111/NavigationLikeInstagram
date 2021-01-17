package com.anton111111.navigation.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.anton111111.navigation.R
import com.anton111111.navigation.databinding.HomeFragmentBinding

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

    override fun onResume() {
        super.onResume()
        Log.e("!!!!", "!!!!onResume: $this")
    }
}