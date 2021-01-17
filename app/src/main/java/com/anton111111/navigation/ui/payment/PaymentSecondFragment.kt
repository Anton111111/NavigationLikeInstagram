package com.anton111111.navigation.ui.payment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.anton111111.navigation.R
import com.anton111111.navigation.databinding.PaymentSecondFragmentBinding

class PaymentSecondFragment : Fragment(R.layout.payment_second_fragment) {

    private val viewBinding: PaymentSecondFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {

            paymentFailBtn.setOnClickListener {
                onComplete(
                    isSuccess = false
                )
            }
            paymentSuccessBtn.setOnClickListener {
                onComplete(
                    isSuccess = true,
                    successWithLogin = false
                )
            }
            paymentSuccesWithLoginBtn.setOnClickListener {
                onComplete(
                    isSuccess = true,
                    successWithLogin = true
                )
            }
        }
    }

    private fun onComplete(successWithLogin: Boolean = false, isSuccess: Boolean = false) {
        findNavController().navigate(
            PaymentSecondFragmentDirections.actionPaymentSecondToPayment(
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