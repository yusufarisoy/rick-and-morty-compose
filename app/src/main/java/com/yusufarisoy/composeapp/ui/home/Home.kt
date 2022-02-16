package com.yusufarisoy.composeapp.ui.home

import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.yusufarisoy.composeapp.R
import com.yusufarisoy.composeapp.data.Character
import com.yusufarisoy.composeapp.data.Location
import com.yusufarisoy.composeapp.domain.home.LocationsUiModelMapper
import com.yusufarisoy.composeapp.ui.NavigationGraph
import com.yusufarisoy.composeapp.ui.home.HomeViewModel.HomeState
import com.yusufarisoy.composeapp.ui.theme.Black800
import com.yusufarisoy.composeapp.ui.theme.Gray600
import com.yusufarisoy.composeapp.ui.theme.Gray700
import com.yusufarisoy.composeapp.utils.CharacterCardSimpleText
import com.yusufarisoy.composeapp.utils.CharacterCardTitle
import com.yusufarisoy.composeapp.utils.PageContent

@Composable
@ExperimentalMaterialApi
fun Home(
    viewModel: HomeViewModel,
    navController: NavController,
    paddingValues: PaddingValues
) {
    val state = viewModel.stateFlow.value
    PageContent(
        isLoading = state.progress,
        modifier = Modifier.fillMaxSize()
    ) {
        HomeScreen(navController, paddingValues, state.uiState)
    }
}

@Composable
@ExperimentalMaterialApi
private fun HomeScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    uiState: HomeState
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        uiState.locationsUiModel?.let { locationsUiModel ->
            item { Header(locationsUiModel) }
        }

        uiState.charactersUiModel?.let {
            item { Title("Characters", it.characterCount) }
            items(it.characters.windowed(2, 2, true)) { sublist ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    sublist.forEach { character ->
                        var isExpanded by remember { mutableStateOf(false) }
                        val large = Size(width = 1f, 330f)
                        val small = Size(width = 1f, 280f)
                        val size: Size by animateSizeAsState(
                            targetValue = if (isExpanded) large else small
                        )
                        Surface(
                            elevation = 4.dp,
                            modifier = Modifier
                                .height(size.height.dp)
                                .fillParentMaxWidth(.5f)
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(0.8.dp, Gray600),
                            onClick = { navController.navigate("${NavigationGraph.CharacterDetail.route}/${character.id}") }
                        ) {
                            CharacterCard(character, isExpanded) { isExpanded = !isExpanded }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(locationsUiModel: LocationsUiModelMapper.LocationsUiModel) {
    Title("Popular Locations", 20)
    Spacer(modifier = Modifier.height(8.dp))
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(locationsUiModel.locations) { location ->
            Banner(location)
        }
    }
}

@Composable
private fun Banner(location: Location) {
    Surface(
        color = Gray700,
        elevation = 6.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .width(250.dp)
            .height(100.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = location.name,
                color = Black800,
                style = MaterialTheme.typography.body2,
                fontSize = 20.sp,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
@ExperimentalMaterialApi
private fun CharacterCard(character: Character, isExpanded: Boolean, onExpandClicked: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = rememberImagePainter(character.image),
            contentDescription = character.name,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
        )
        CharacterCardTitle(text = character.name)

        val color = when(character.status) {
            "Alive" -> Color.Green
            "Dead" -> Color.Red
            else -> Gray600
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 1.dp)) {
            Icon(
                painterResource(R.drawable.ic_circle),
                contentDescription = "",
                tint = color,
                modifier = Modifier.scale(0.6f)
            )
            Text(text = character.status, color = color)
        }
        CharacterCardSimpleText(text = "${character.species} - ${character.gender}")

        if (isExpanded) {
            CharacterCardSimpleText(text = character.location.name)
            CharacterCardSimpleText(text = "Created at: ${character.created.slice(0..9)}")
        }

        IconButton(onClick = { onExpandClicked() }) {
            if (isExpanded.not()) {
                Icon(Icons.Filled.KeyboardArrowDown, "")
            } else {
                Icon(Icons.Filled.KeyboardArrowUp, "")
            }
        }
    }
}

@Composable
private fun Title(text: String, count: Int) {
    Text(
        text = "$text ($count)",
        modifier = Modifier.padding(horizontal = 8.dp),
        style = TextStyle(Black800, 18.sp, FontWeight.Bold)
    )
}
