package com.anton111111.navigation.ui.splash

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.anton111111.navigation.IS_HAS_USER_FIELD

class SplashViewModel(private val prefs: SharedPreferences) : ViewModel() {

    val isHasUser: Boolean
        get() {
            return prefs.getBoolean(IS_HAS_USER_FIELD, false)
        }

}