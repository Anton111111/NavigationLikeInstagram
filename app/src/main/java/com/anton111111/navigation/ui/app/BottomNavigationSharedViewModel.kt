package com.anton111111.navigation.ui.app

import androidx.annotation.IdRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class BottomNavigationSharedViewModel : ViewModel() {
    private val _bottomNavActionSharedState: MutableSharedFlow<BottomNavAction> =
        MutableSharedFlow()
    val bottomNavActionSharedState = _bottomNavActionSharedState.asSharedFlow()

    /**
     * Navigate to certain bottom navigation item and navigate to certain destination in here.
     * @param bottomNavItemId bottom nav id (from bottom_nav.xml)
     * @param destinationId if not null will navigate to this destination after bottom nav changed item
     */
    fun navigate(
        @IdRes bottomNavItemId: Int,
        @IdRes destinationId: Int? = null
    ) {
        viewModelScope.launch {
            _bottomNavActionSharedState.emit(
                BottomNavAction(
                    bottomNavItemId = bottomNavItemId,
                    destinationId = destinationId
                )
            )
        }
    }

    data class BottomNavAction(
        @IdRes val bottomNavItemId: Int,
        @IdRes val destinationId: Int? = null
    )
}