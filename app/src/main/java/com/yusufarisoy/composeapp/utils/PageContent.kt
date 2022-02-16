package com.yusufarisoy.composeapp.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.yusufarisoy.composeapp.ui.theme.Red300

@Composable
fun PageContent(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(isLoading) {
        Box(modifier = modifier) {
            content()
        }
    }
}

@Composable
private fun Layout(
    isLoading: Boolean,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    var height by remember { mutableStateOf(0.dp) }
    var width by remember { mutableStateOf(0.dp) }

    Box {
        Surface(modifier = Modifier.onSizeChanged {
            with(density) {
                height = it.height.toDp()
                width = it.width.toDp()
            }
        }) {
            content()
        }
        if (isLoading) {
            Surface(
                color = Color.Transparent,
                modifier = Modifier
                    .size(
                        height = height,
                        width = width
                    )
                    .background(Color(0xCCFFFFFD))
                    .align(Alignment.Center)
            ) {
                LoadingScreen()
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.padding(top = 62.dp)) {
        CircularProgressIndicator(
            color = Red300,
            strokeWidth = 5.dp,
            modifier = Modifier.height(62.dp).width(62.dp))
    }
}
