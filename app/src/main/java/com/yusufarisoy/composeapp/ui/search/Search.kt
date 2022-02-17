package com.yusufarisoy.composeapp.ui.search

import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.yusufarisoy.composeapp.R
import com.yusufarisoy.composeapp.data.Character
import com.yusufarisoy.composeapp.domain.home.CharactersUiModelMapper.CharactersUiModel
import com.yusufarisoy.composeapp.ui.NavigationGraph
import com.yusufarisoy.composeapp.ui.search.SearchViewModel.SearchState
import com.yusufarisoy.composeapp.ui.theme.Black800
import com.yusufarisoy.composeapp.ui.theme.Gray100
import com.yusufarisoy.composeapp.ui.theme.Gray600
import com.yusufarisoy.composeapp.ui.theme.Red300
import com.yusufarisoy.composeapp.utils.CharacterCardSimpleText
import com.yusufarisoy.composeapp.utils.PageContent
import kotlinx.coroutines.FlowPreview

@Composable
@FlowPreview
@ExperimentalMaterialApi
fun Search(
    viewModel: SearchViewModel,
    navController: NavController,
    paddingValues: PaddingValues
) {
    val state = viewModel.stateFlow.value
    PageContent(
        isLoading = state.progress,
        modifier = Modifier.fillMaxSize()
    ) {
        SearchScreen(viewModel, navController, paddingValues, state.uiState)
    }
}

@Composable
@FlowPreview
@ExperimentalMaterialApi
private fun SearchScreen(
    viewModel: SearchViewModel,
    navController: NavController,
    paddingValues: PaddingValues,
    uiState: SearchState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SearchView(uiState.searchQuery, viewModel::onQueryChanged)

        if (uiState.charactersUiModel != null && uiState.charactersUiModel.characters.isNotEmpty()) {
            SuccessResultState(uiState.charactersUiModel) { characterId ->
                navController.navigate("${NavigationGraph.CharacterDetail.route}/${characterId}")
            }
        } else {
            EmptyState()
        }
    }
}

@Composable
private fun SearchView(searchQuery: String, onQueryChanged: (query: String) -> Unit) {
    TextField(
        value = searchQuery,
        singleLine = true,
        onValueChange = { value -> onQueryChanged(value) },
        placeholder = { Text(stringResource(R.string.search)) },
        shape = RoundedCornerShape(16.dp),
        textStyle = TextStyle(fontSize = 17.sp, color = Black800),
        leadingIcon = { Icon(Icons.Filled.Search, null, tint = Color.Gray) },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = Color.White,
            cursorColor = Color.DarkGray
        ),
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .border(0.8.dp, Color.DarkGray, RoundedCornerShape(28.dp))
    )
}

@Composable
private fun EmptyState() {
    Text(text = stringResource(R.string.search_default_state), fontWeight = FontWeight.Bold, fontSize = 16.sp)
    Image(
        painter = painterResource(id = R.drawable.search_holder),
        modifier = Modifier.fillMaxWidth(),
        contentDescription = stringResource(R.string.search_default_state)
    )
}

@Composable
@ExperimentalMaterialApi
private fun SuccessResultState(
    charactersUiModel: CharactersUiModel,
    onCharacterClicked: (id: String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                text = "${charactersUiModel.characterCount} results found",
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(charactersUiModel.characters) { character ->
            CharacterCard(character, onCharacterClicked)
        }
    }
}

@Composable
@ExperimentalMaterialApi
private fun CharacterCard(character: Character, onCharacterClicked: (id: String) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }
    val large = Size(width = 150f, 170f)
    val small = Size(width = 120f, 120f)
    val size: Size by animateSizeAsState(targetValue = if (isExpanded) large else small)
    val color = when(character.status) {
        "Alive" -> Color.Green
        "dead" -> Red300
        else -> Gray600
    }
    Surface(
        elevation = 4.dp,
        modifier = Modifier
            .height(size.height.dp)
            .fillMaxWidth()
            .padding(2.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.5.dp, Gray100),
        onClick = { onCharacterClicked(character.id.toString()) }
    ) {
        Row {
            Image(
                painter = rememberImagePainter(character.image),
                contentDescription = character.name,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(size.width.dp)
                    .fillMaxHeight()
            )

            Column {
                Row(modifier = Modifier.padding(8.dp)) {
                    Icon(
                        painterResource(R.drawable.ic_circle),
                        contentDescription = "",
                        tint = color,
                        modifier = Modifier.scale(0.8f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = character.name,
                        style = TextStyle(Black800, 16.sp, FontWeight.Bold)
                    )
                }

                CharacterCardSimpleText(text = character.gender)
                CharacterCardSimpleText(text = character.species)

                if (isExpanded) {
                    CharacterCardSimpleText(text = character.location.name)
                    CharacterCardSimpleText(text = "Created at: ${character.created.slice(0..9)}")
                }

                IconButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { isExpanded = !isExpanded }
                ) {
                    if (isExpanded.not()) {
                        Icon(Icons.Filled.ArrowDropDown, "")
                    } else {
                        Icon(Icons.Filled.KeyboardArrowUp, "")
                    }
                }
            }
        }
    }
}
