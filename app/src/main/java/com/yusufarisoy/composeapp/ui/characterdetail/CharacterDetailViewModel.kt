package com.yusufarisoy.composeapp.ui.characterdetail

import com.yusufarisoy.composeapp.data.Character
import com.yusufarisoy.composeapp.data.local.FavoriteCharacter
import com.yusufarisoy.composeapp.database.FavoriteCharacterDao
import com.yusufarisoy.composeapp.domain.CharacterRepository
import com.yusufarisoy.composeapp.domain.characterdetail.CharacterDetailUiModelMapper.CharacterDetailUiModel
import com.yusufarisoy.composeapp.ui.characterdetail.CharacterDetailViewModel.CharacterDetailState
import com.yusufarisoy.composeapp.utils.StatefulViewModel
import com.yusufarisoy.composeapp.utils.UiState
import com.yusufarisoy.composeapp.utils.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val favoriteCharacterDao: FavoriteCharacterDao
) : StatefulViewModel<CharacterDetailState>(CharacterDetailState()) {

    fun fetchCharacter(id: Int) = launch(notifyProgress = false) {
        val response = characterRepository.getCharacterById(id)
        val favorite = favoriteCharacterDao.getById(response.character.id)
        val isFavorite = favorite != null
        response.isFavorite = isFavorite
        setState { copy(characterUiModel = response) }
    }

    fun onFavoriteClicked() {
        currentUiState.characterUiModel?.let { characterUiModel ->
            launch(notifyProgress = false) {
                val character = characterUiModel.character.toFavoriteCharacter()
                var showSnackBar = false
                if (characterUiModel.isFavorite) {
                    showSnackBar = true
                    favoriteCharacterDao.delete(character)
                } else {
                    favoriteCharacterDao.add(character)
                }
                setState {
                    copy(
                        showSnackBar = showSnackBar,
                        characterUiModel = characterUiModel
                            .copy(isFavorite = !characterUiModel.isFavorite)
                    )
                }
            }
        }
    }

    fun snackBarShowed() {
        setState { copy(showSnackBar = false) }
    }

    private fun Character.toFavoriteCharacter(): FavoriteCharacter = FavoriteCharacter(
        this.id,
        this.name,
        this.status,
        this.species,
        this.gender,
        this.location.name,
        this.image,
        this.created
    )

    data class CharacterDetailState(
        val characterUiModel: CharacterDetailUiModel? = null,
        val showSnackBar: Boolean = false
    ) : UiState
}
