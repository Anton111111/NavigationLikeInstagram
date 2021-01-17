package com.anton111111.navigation.ui.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anton111111.navigation.R
import com.anton111111.navigation.databinding.AppActivityBinding

class AppActivity : AppCompatActivity(R.layout.app_activity) {

    private val viewBinding: AppActivityBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}