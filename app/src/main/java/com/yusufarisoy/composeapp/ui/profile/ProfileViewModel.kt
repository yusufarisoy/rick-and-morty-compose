package com.yusufarisoy.composeapp.ui.profile

import com.yusufarisoy.composeapp.data.User
import com.yusufarisoy.composeapp.data.local.FavoriteCharacter
import com.yusufarisoy.composeapp.database.FavoriteCharacterDao
import com.yusufarisoy.composeapp.domain.UserStore
import com.yusufarisoy.composeapp.ui.profile.ProfileViewModel.ProfileState
import com.yusufarisoy.composeapp.utils.StatefulViewModel
import com.yusufarisoy.composeapp.utils.UiState
import com.yusufarisoy.composeapp.utils.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userStore: UserStore,
    private val favoriteCharacterDao: FavoriteCharacterDao
) : StatefulViewModel<ProfileState>(ProfileState()) {

    init {
        launch(notifyProgress = false) {
            getFavorites()
        }
    }

    fun fetchUser(userId: String?) {
        userId?.let {
            val user = userStore.getSession()
            setState { copy(user = user) }
        }
    }

    private suspend fun getFavorites() {
        val favorites = favoriteCharacterDao.getFavorites()
        setState { copy(favorites = favorites) }
    }

    fun removeFavorite(character: FavoriteCharacter) = launch {
        val favorites = currentUiState.favorites?.toMutableList()
        val index = favorites?.indexOfFirst { it == character }
        if (index == -1 || index == null) return@launch
        favorites.removeAt(index)
        setState { copy(favorites = favorites) }
        favoriteCharacterDao.delete(character)
    }

    fun clearFavorites() = launch {
        setState { copy(favorites = listOf()) }
        favoriteCharacterDao.clear()
    }

    data class ProfileState(
        val user: User? = null,
        val favorites: List<FavoriteCharacter>? = null
    ) : UiState
}
