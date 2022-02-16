package com.yusufarisoy.composeapp.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class StatefulViewModel<S : UiState> constructor(initialState: S) : ViewModel() {

    private val stateMutex = Mutex()

    private val _stateFlow: MutableState<PageState<S>> = mutableStateOf(PageState(initialState))
    val stateFlow: State<PageState<S>>
        get() = _stateFlow

    val currentUiState: S
        get() = _stateFlow.value.uiState

    protected fun setState(reducer: S.() -> S) = pushState {
        val newState = _stateFlow.value.copy(uiState = reducer(currentUiState))
        _stateFlow.value = newState
    }

    fun setProgress(showProgress: Boolean) = pushState {
        _stateFlow.value = _stateFlow.value.copy(progress = showProgress)
    }

    fun setError(exception: Exception) = pushState {
        _stateFlow.value = _stateFlow.value.copy(error = exception)
    }.invokeOnCompletion {
        pushState {
            _stateFlow.value = _stateFlow.value.copy(error = null)
        }
    }

    private fun pushState(action: () -> Unit) = viewModelScope.launch {
        stateMutex.withLock {
            action.invoke()
        }
    }
}

data class PageState<T : UiState>(
    val uiState: T,
    val progress: Boolean = false,
    val error: Exception? = null
)

interface UiState