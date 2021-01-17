package com.anton111111.navigation.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anton111111.navigation.R
import com.anton111111.navigation.databinding.SearchFragmentBinding

class SearchFragment : Fragment(R.layout.search_fragment) {
    private val viewBinding: SearchFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.toSecondScreenBtn.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.actionSearchToSecond())
        }
    }
}

