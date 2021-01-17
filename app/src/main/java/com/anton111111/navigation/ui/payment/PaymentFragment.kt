package com.anton111111.navigation.ui.payment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.anton111111.navigation.IS_HAS_USER_FIELD
import com.anton111111.navigation.R
import com.anton111111.navigation.databinding.PaymentFragmentBinding
import org.koin.android.ext.android.inject

class PaymentFragment : Fragment(R.layout.payment_fragment) {

    private val navArgs: PaymentFragmentArgs by navArgs()
    private val viewBinding: PaymentFragmentBinding by viewBinding()
    private val prefs: SharedPreferences by inject()
    private val isHasUser: Boolean
        get() = prefs.getBoolean(IS_HAS_USER_FIELD, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (navArgs.isSuccess) {
            onComplete(
                successWithLogin = navArgs.successWithLogin,
                isSuccess = navArgs.isSuccess
            )
        }
        with(viewBinding) {
            secondPaymentScreenBtn.setOnClickListener {
                findNavController().navigate(PaymentFragmentDirections.actionPaymentToPaymentSecond())
            }
        }
    }

    private fun onComplete(successWithLogin: Boolean = false, isSuccess: Boolean = false) {
        if (isHasUser)
            findNavController().navigate(PaymentFragmentDirections.actionPaymentToHome())
        else
            findNavController().navigate(
                PaymentFragmentDirections.actionPaymentToAuth(
                    isSuccess = isSuccess,
                    successWithLogin = successWithLogin
                )
            )
    }

    override fun onResume() {
        super.onResume()
        Log.e("!!!!", "!!!!onResume: $this")
    }
}