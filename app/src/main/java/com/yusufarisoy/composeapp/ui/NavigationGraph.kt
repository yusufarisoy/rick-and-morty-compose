package com.yusufarisoy.composeapp.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yusufarisoy.composeapp.ui.characterdetail.CharacterDetail
import com.yusufarisoy.composeapp.ui.characterdetail.CharacterDetailViewModel
import com.yusufarisoy.composeapp.ui.home.Home
import com.yusufarisoy.composeapp.ui.home.HomeViewModel
import com.yusufarisoy.composeapp.ui.profile.Profile
import com.yusufarisoy.composeapp.ui.profile.ProfileViewModel
import com.yusufarisoy.composeapp.ui.search.Search
import com.yusufarisoy.composeapp.ui.search.SearchViewModel
import kotlinx.coroutines.FlowPreview

@Composable
@FlowPreview
@ExperimentalMaterialApi
fun NavigationHost(
    mainViewModel: MainViewModel,
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = NavigationGraph.Home.route
    ) {
        composable(
            route = NavigationGraph.Home.route
        ) {
            mainViewModel.onTabSelected(NavigationItem.Home)
            val viewModel: HomeViewModel = hiltViewModel()
            Home(viewModel, navController, paddingValues)
        }

        composable(
            route = NavigationGraph.Search.route
        ) {
            mainViewModel.onTabSelected(NavigationItem.Search)
            val viewModel = hiltViewModel<SearchViewModel>()
            Search(viewModel, navController, paddingValues)
        }

        composable(
            route = NavigationGraph.Profile.routeWithArgument,
            arguments = listOf(
                navArgument(NavigationGraph.Profile.userId) { type = NavType.StringType }
            )
        ) {
            val viewModel = hiltViewModel<ProfileViewModel>()
            Profile(viewModel, paddingValues, it.arguments?.getString(NavigationGraph.Profile.userId))
        }

        composable(
            route = NavigationGraph.CharacterDetail.routeWithArgument,
            arguments = listOf(
                navArgument(NavigationGraph.CharacterDetail.characterId) { type = NavType.IntType }
            )
        ) {
            val viewModel = hiltViewModel<CharacterDetailViewModel>()
            CharacterDetail(viewModel, it.arguments?.getInt(NavigationGraph.CharacterDetail.characterId))
        }
    }
}

sealed class NavigationGraph(val route: String) {

    object Home : NavigationGraph("home")

    object Search : NavigationGraph("search")

    object Profile : NavigationGraph("user") {
        const val routeWithArgument: String = "user/{id}"
        const val userId: String = "id"
    }

    object CharacterDetail : NavigationGraph("character") {
        const val routeWithArgument: String = "character/{id}"
        const val characterId: String = "id"
    }
}

sealed class NavigationItem(val route: String, val title: String, val icon: ImageVector) {

    object Home : NavigationItem(NavigationGraph.Home.route, "Home", Icons.Outlined.Home)
    object Search : NavigationItem(NavigationGraph.Search.route, "Search", Icons.Outlined.Search)
}
