package com.yusufarisoy.composeapp.domain

import com.yusufarisoy.composeapp.ui.NavigationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BottomNavigationTabStore @Inject constructor() {

    private val _state = MutableStateFlow<NavigationItem>(NavigationItem.Home)
    val state: StateFlow<NavigationItem>
        get() = _state

    fun onTabSelected(selectedTab: NavigationItem) {
        _state.value = selectedTab
    }
}
