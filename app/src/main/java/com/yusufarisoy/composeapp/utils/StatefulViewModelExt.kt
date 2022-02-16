package com.yusufarisoy.composeapp.utils

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun StatefulViewModel<*>.launch(
    notifyProgress: Boolean = true,
    block: suspend CoroutineScope.() -> Unit
) = viewModelScope.launch {
    if (notifyProgress) {
        setProgress(showProgress = true)
    }

    try {
        block()
    } catch (exception: Exception) {
        setError(exception)
    } finally {
        if (notifyProgress) {
            setProgress(showProgress = false)
        }
    }
}
