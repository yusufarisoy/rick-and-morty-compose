package com.yusufarisoy.composeapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.yusufarisoy.composeapp.R
import com.yusufarisoy.composeapp.data.User
import com.yusufarisoy.composeapp.ui.theme.Black800
import com.yusufarisoy.composeapp.ui.theme.Gray700
import com.yusufarisoy.composeapp.ui.theme.Gray800
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@Composable
@FlowPreview
@ExperimentalMaterialApi
@ExperimentalAnimationApi
fun App(mainViewModel: MainViewModel) {
    val scope = rememberCoroutineScope()
    val state = mainViewModel.stateFlow.value
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val backButtonState = rememberSaveable { mutableStateOf(true) }
    val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)

    BottomDrawer(
        drawerState = drawerState,
        drawerElevation = 4.dp,
        drawerContentColor = Gray700,
        gesturesEnabled = drawerState.isOpen,
        drawerShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        drawerContent = { DrawerContent(scope, drawerState, backButtonState) }
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { TopBar(state.uiState.user, navController, backButtonState) },
            floatingActionButton = { AddPostFAB { scope.launch { drawerState.open() } } },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true,
            bottomBar = {
                BottomNavigation(
                    navController,
                    state.uiState.selectedTab,
                    mainViewModel::onTabSelected
                )
            }
        ) { paddingValues ->
            NavigationHost(mainViewModel, navController, scaffoldState, paddingValues)
        }
    }
}

@Composable
@ExperimentalAnimationApi
fun TopBar(
    user: User?,
    navController: NavController,
    backButtonState: MutableState<Boolean>
) {
    TopAppBar(
        elevation = 6.dp,
        backgroundColor = Black800,
        modifier = Modifier.height(58.dp),
        title = { Text(text = stringResource(R.string.app_name), color = Color.White) },
        navigationIcon = {
            AnimatedVisibility(
                visible = backButtonState.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.profile),
                        tint = Color.White
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = {
                user?.id?.let { userId ->
                    navController.navigate("${NavigationGraph.Profile.route}/${userId}") {
                        launchSingleTop = true
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = stringResource(R.string.profile),
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun BottomNavigation(
    navController: NavController,
    selectedTab: NavigationItem?,
    onTabSelected: (selectedTab: NavigationItem) -> Unit
) {
    BottomNavigation(backgroundColor = Black800) {
        val navigationItems = listOf(NavigationItem.Home, NavigationItem.Search)
        navigationItems.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title, fontSize = 11.sp) },
                selectedContentColor = Color.Cyan,
                unselectedContentColor = Color.White,
                selected = selectedTab == item,
                onClick = {
                    onTabSelected(item)
                    navController.navigate(item.route) {
                        popUpTo(item.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun AddPostFAB(buttonClicked: () -> Unit) {
    FloatingActionButton(onClick = { buttonClicked() }, backgroundColor = Gray800) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.new_post),
            modifier = Modifier.size(35.dp)
        )
    }
}

@Composable
@ExperimentalMaterialApi
fun DrawerContent(
    scope: CoroutineScope,
    drawerState: BottomDrawerState,
    backButtonState: MutableState<Boolean>
) {
    Button(
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        onClick = {
            scope.launch { drawerState.close() }
            backButtonState.value = !backButtonState.value
        }
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(R.string.new_post))
        Text(text = stringResource(R.string.new_post))
    }
    Button(
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        onClick = { scope.launch { drawerState.close() } }
    ) {
        Icon(imageVector = Icons.Filled.Send, contentDescription = stringResource(R.string.new_story))
        Text(text = stringResource(R.string.new_story))
    }
}
