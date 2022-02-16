package com.yusufarisoy.composeapp.ui

import androidx.lifecycle.viewModelScope
import com.yusufarisoy.composeapp.data.User
import com.yusufarisoy.composeapp.domain.BottomNavigationTabStore
import com.yusufarisoy.composeapp.domain.UserStore
import com.yusufarisoy.composeapp.ui.MainViewModel.MainState
import com.yusufarisoy.composeapp.utils.StatefulViewModel
import com.yusufarisoy.composeapp.utils.UiState
import com.yusufarisoy.composeapp.utils.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userStore: UserStore,
    private val navigationTabStore: BottomNavigationTabStore
) : StatefulViewModel<MainState>(MainState()) {

    init {
        launch { logIn() }
        observeTabSelection()
        setState { copy(selectedTab = navigationTabStore.state.value) }
    }

    private suspend fun logIn() {
        delay(1200)
        val user = User(id = "1", name = "Yusuf", surname = "Arisoy", imageUrl = "")
        userStore.createSession(user)
        setState { copy(user = user) }
    }

    private fun observeTabSelection() {
        navigationTabStore.state.onEach { selectedTab ->
            setState { copy(selectedTab = selectedTab) }
        }.launchIn(viewModelScope)
    }

    fun onTabSelected(selectedTab: NavigationItem) {
        navigationTabStore.onTabSelected(selectedTab)
    }

    data class MainState(
        val user: User? = null,
        val selectedTab: NavigationItem? = null
    ) : UiState
}
