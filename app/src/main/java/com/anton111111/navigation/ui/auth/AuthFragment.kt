package com.anton111111.navigation.ui.auth

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.anton111111.navigation.IS_HAS_USER_FIELD
import com.anton111111.navigation.R
import com.anton111111.navigation.databinding.AuthFragmentBinding
import org.koin.android.ext.android.inject

class AuthFragment : Fragment(R.layout.auth_fragment) {

    private val navArgs: AuthFragmentArgs by navArgs()
    private val viewBinding: AuthFragmentBinding by viewBinding()
    private val prefs: SharedPreferences by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (navArgs.isSuccess && navArgs.successWithLogin)
            login()

        with(viewBinding) {
            loginBtn.setOnClickListener {
                login()
            }
            paymentBtn.setOnClickListener {
                findNavController().navigate(AuthFragmentDirections.actionAuthToPayment())
            }
        }
    }


    private fun login() {
        prefs.edit().putBoolean(IS_HAS_USER_FIELD, true).apply()
        findNavController().navigate(AuthFragmentDirections.actionAuthToUserNavigation())
    }

    override fun onResume() {
        super.onResume()
        Log.e("!!!!", "!!!!onResume: $this")
    }

}