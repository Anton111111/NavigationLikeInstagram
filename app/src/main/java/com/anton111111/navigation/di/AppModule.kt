package com.anton111111.navigation.di

import android.content.Context
import com.anton111111.navigation.ui.app.BottomNavigationSharedViewModel
import com.anton111111.navigation.ui.splash.SplashViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { BottomNavigationSharedViewModel() }
    single { androidApplication().getSharedPreferences("settings", Context.MODE_PRIVATE) }
}