package com.anton111111.navigation.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.anton111111.navigation.R
import com.anton111111.navigation.databinding.HomeSecondFragmentBinding

class HomeFragmentSecond : Fragment(R.layout.home_second_fragment) {

    private val viewBinding: HomeSecondFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        Log.e("!!!!", "!!!!onResume: $this")
    }
}