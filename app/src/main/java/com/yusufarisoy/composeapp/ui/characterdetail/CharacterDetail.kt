package com.yusufarisoy.composeapp.ui.characterdetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.yusufarisoy.composeapp.R
import com.yusufarisoy.composeapp.domain.characterdetail.CharacterDetailUiModelMapper.CharacterDetailUiModel
import com.yusufarisoy.composeapp.ui.characterdetail.CharacterDetailViewModel.CharacterDetailState
import com.yusufarisoy.composeapp.ui.theme.Black800
import com.yusufarisoy.composeapp.ui.theme.Gray600
import com.yusufarisoy.composeapp.utils.PageContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CharacterDetail(
    viewModel: CharacterDetailViewModel,
    scaffoldState: ScaffoldState
) {
    val state = viewModel.stateFlow.value
    PageContent(isLoading = state.progress) {
        CharacterDetailPage(viewModel, state.uiState, scaffoldState)
    }
}

@Composable
private fun CharacterDetailPage(
    viewModel: CharacterDetailViewModel,
    uiState: CharacterDetailState,
    scaffoldState: ScaffoldState
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        uiState.characterUiModel?.let { characterUiModel ->
            Character(
                characterUiModel,
                uiState.characterUiModel.isFavorite,
                viewModel::onFavoriteClicked
            )
        }
    }

    if (uiState.showSnackBar) {
        val character = uiState.characterUiModel?.character?.name
        character?.let {
            showSnackBar(scope, scaffoldState, it, viewModel::onFavoriteClicked)
            viewModel.snackBarShowed()
        }
    }
}

@Composable
private fun Character(
    characterUiModel: CharacterDetailUiModel,
    isFavorite: Boolean,
    onFavoriteClicked: () -> Unit
) = with(characterUiModel) {
    Image(
        painter = rememberImagePainter(character.image),
        contentDescription = character.name,
        modifier = Modifier
            .size(200.dp)
            .clip(RoundedCornerShape(2.dp))
    )
    Spacer(modifier = Modifier.height(16.dp))
    Surface(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        border = BorderStroke(0.8.dp, Black800),
        modifier = Modifier.fillMaxSize(),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = character.name,
                    style = TextStyle(Black800, 22.sp, FontWeight.Bold),
                    modifier = Modifier.padding(end = 4.dp)
                )
                IconButton(onClick = onFavoriteClicked) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = stringResource(R.string.add_to_favorites),
                        tint = if (isFavorite) Color.Red else Gray600,
                        modifier = Modifier.scale(1.3f)
                    )
                }
            }
            val color = when(character.status) {
                "Alive" -> Color.Green
                "Dead" -> Color.Red
                else -> Gray600
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    painterResource(R.drawable.ic_circle),
                    contentDescription = "",
                    tint = color,
                    modifier = Modifier.scale(0.6f)
                )
                Text(text = character.status, color = color, fontSize = 16.sp)
            }
            Text(text = "Species: ${character.species}", fontSize = 16.sp)
            Text(text = "Gender: ${character.gender}", fontSize = 16.sp)
            Text(text = "Created at: ${character.created.slice(0..8)}}", fontSize = 16.sp)
            Text(text = "Last known location: ${character.location.name}", fontSize = 16.sp)
        }
    }
}

private fun showSnackBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    character: String,
    onActionPerformed: () -> Unit
) {
    scope.launch {
        val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
            message = "$character is removed from favorites.",
            actionLabel = "Undo"
        )
        if (snackBarResult == SnackbarResult.ActionPerformed) {
            onActionPerformed()
        }
    }
}
