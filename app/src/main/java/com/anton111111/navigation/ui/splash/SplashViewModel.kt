package com.anton111111.navigation.ui.splash

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anton111111.navigation.IS_HAS_USER_FIELD
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(private val prefs: SharedPreferences) : ViewModel() {

    private val _isHasUser: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isHasUser = _isHasUser.asStateFlow()

    init {
        //Emulate long request
        viewModelScope.launch {
            delay(2000)
            _isHasUser.value = prefs.getBoolean(IS_HAS_USER_FIELD, false)
        }
    }

}