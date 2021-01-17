package com.anton111111.navigation.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.anton111111.navigation.R
import com.anton111111.navigation.databinding.SearchFragmentBinding

class SearchFragment : Fragment(R.layout.search_fragment) {

    private val viewBinding: SearchFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        Log.e("!!!!", "!!!!onResume: $this")
    }

}