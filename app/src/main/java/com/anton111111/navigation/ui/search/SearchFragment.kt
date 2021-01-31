package com.anton111111.navigation.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anton111111.navigation.R
import com.anton111111.navigation.databinding.SearchFragmentBinding
import com.anton111111.navigation.ui.app.BottomNavigationSharedViewModel
import com.anton111111.navigation.ui.app.UserNavigationFragmentDirections
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchFragment : Fragment(R.layout.search_fragment) {
    private val viewBinding: SearchFragmentBinding by viewBinding()
    private val bottomNavViewModel: BottomNavigationSharedViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            toSecondScreenBtn.setOnClickListener {
                findNavController().navigate(SearchFragmentDirections.actionSearchToSecond())
            }

            toPaymentScreenBtn.setOnClickListener {
                Navigation.findNavController(requireActivity(), R.id.root_nav_host)
                    .navigate(UserNavigationFragmentDirections.actionUserToPayment())
            }

            toProfileBtn.setOnClickListener {
                bottomNavViewModel.navigate(R.id.profile_navigation)
            }

            toSecondHomeScreenBtn.setOnClickListener {
                bottomNavViewModel.navigate(R.id.home_navigation, R.id.home_fragment_second)
            }
        }


    }
}

