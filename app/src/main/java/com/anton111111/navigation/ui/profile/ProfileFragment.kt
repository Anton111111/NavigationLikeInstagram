package com.anton111111.navigation.ui.profile

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.anton111111.navigation.IS_HAS_USER_FIELD
import com.anton111111.navigation.R
import com.anton111111.navigation.databinding.ProfileFragmentBinding
import org.koin.android.ext.android.inject

class ProfileFragment : Fragment(R.layout.profile_fragment) {

    private val viewBinding: ProfileFragmentBinding by viewBinding()
    private val prefs: SharedPreferences by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding) {
            logoutBtn.setOnClickListener { logout() }
        }
    }

    private fun logout() {
        prefs.edit().putBoolean(IS_HAS_USER_FIELD, false).apply()
        val intent = requireActivity().intent
        requireActivity().finish()
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        Log.e("!!!!", "!!!!onResume: $this")
    }

}