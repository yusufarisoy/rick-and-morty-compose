package com.yusufarisoy.composeapp.ui.search

import androidx.lifecycle.viewModelScope
import com.yusufarisoy.composeapp.domain.CharacterRepository
import com.yusufarisoy.composeapp.domain.home.CharactersUiModelMapper.CharactersUiModel
import com.yusufarisoy.composeapp.ui.search.SearchViewModel.SearchState
import com.yusufarisoy.composeapp.utils.StatefulViewModel
import com.yusufarisoy.composeapp.utils.UiState
import com.yusufarisoy.composeapp.utils.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : StatefulViewModel<SearchState>(SearchState()) {

    private var queryFlow: MutableSharedFlow<String> = MutableSharedFlow(extraBufferCapacity = 1)

    init {
        listenQueryChanges()
    }

    private fun listenQueryChanges() {
        queryFlow
            .debounce(DEBOUNCE_MS)
            .distinctUntilChanged()
            .onEach { query -> fetchSearch(query) }
            .launchIn(viewModelScope)
    }

    fun onQueryChanged(query: String) {
        if (query.length > 2) {
            queryFlow.tryEmit(query)
            setState { copy(searchQuery = query) }
        } else {
            setState { copy(searchQuery = query, charactersUiModel = null) }
        }
    }

    private fun fetchSearch(query: String) = launch {
        val response = characterRepository.getCharacters(query)
        delay(300L)
        setState { copy(charactersUiModel = response) }
    }

    data class SearchState(
        val searchQuery: String = "",
        val charactersUiModel: CharactersUiModel? = null
    ) : UiState

    companion object {
        private const val DEBOUNCE_MS = 400L
    }
}
