package com.yusufarisoy.composeapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.yusufarisoy.composeapp.ui.splash.Splash
import com.yusufarisoy.composeapp.ui.theme.ComposeAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@FlowPreview
@AndroidEntryPoint
@ExperimentalMaterialApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = mainViewModel.stateFlow.value
            val isDarkTheme = remember { mutableStateOf(false) }
            ComposeAppTheme(darkTheme = isDarkTheme.value) {
                if (state.uiState.user == null) {
                    Splash()
                } else {
                    App(mainViewModel)
                }
            }
        }
    }
}
