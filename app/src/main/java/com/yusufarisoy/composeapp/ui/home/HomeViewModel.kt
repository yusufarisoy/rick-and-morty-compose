package com.yusufarisoy.composeapp.ui.home

import com.yusufarisoy.composeapp.domain.CharacterRepository
import com.yusufarisoy.composeapp.domain.LocationRepository
import com.yusufarisoy.composeapp.domain.home.CharactersUiModelMapper.CharactersUiModel
import com.yusufarisoy.composeapp.domain.home.LocationsUiModelMapper.LocationsUiModel
import com.yusufarisoy.composeapp.ui.home.HomeViewModel.HomeState
import com.yusufarisoy.composeapp.utils.StatefulViewModel
import com.yusufarisoy.composeapp.utils.UiState
import com.yusufarisoy.composeapp.utils.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val locationRepository: LocationRepository
) : StatefulViewModel<HomeState>(HomeState()) {

    init {
        fetchHome()
    }

    private fun fetchHome() = launch {
        val charactersResponse = characterRepository.getCharacters()
        val locationsResponse = locationRepository.getLocations()
        setState {
            copy(
                locationsUiModel = locationsResponse,
                charactersUiModel = charactersResponse
            )
        }
    }

    data class HomeState(
        val locationsUiModel: LocationsUiModel? = null,
        val charactersUiModel: CharactersUiModel? = null
    ) : UiState
}
