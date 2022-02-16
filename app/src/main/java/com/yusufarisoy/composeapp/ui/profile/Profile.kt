package com.yusufarisoy.composeapp.ui.profile

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.DismissDirection.EndToStart
import androidx.compose.material.DismissValue.Default
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.yusufarisoy.composeapp.R
import com.yusufarisoy.composeapp.data.User
import com.yusufarisoy.composeapp.data.local.FavoriteCharacter
import com.yusufarisoy.composeapp.ui.profile.ProfileViewModel.ProfileState
import com.yusufarisoy.composeapp.ui.theme.Black800
import com.yusufarisoy.composeapp.ui.theme.Gray600
import com.yusufarisoy.composeapp.ui.theme.Gray700
import com.yusufarisoy.composeapp.utils.CharacterCardSimpleText
import com.yusufarisoy.composeapp.utils.CharacterCardTitle
import com.yusufarisoy.composeapp.utils.PageContent
import com.yusufarisoy.composeapp.utils.ShowDialog

@Composable
@ExperimentalMaterialApi
fun Profile(
    viewModel: ProfileViewModel,
    paddingValues: PaddingValues,
    userId: String?
) {
    viewModel.fetchUser(userId)
    val state = viewModel.stateFlow.value
    val showDialog = remember { mutableStateOf(false) }
    PageContent(
        isLoading = state.progress,
        modifier = Modifier.fillMaxSize()
    ) {
        ProfilePage(viewModel, paddingValues, state.uiState, showDialog)
    }
}

@Composable
@ExperimentalMaterialApi
private fun ProfilePage(
    viewModel: ProfileViewModel,
    paddingValues: PaddingValues,
    uiState: ProfileState,
    showDialog: MutableState<Boolean>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        uiState.user?.let { user ->
            ProfileCard(user)
        }

        uiState.favorites?.let { favorites ->
            if (favorites.isNotEmpty()) {
                ResultState(favorites, viewModel::removeFavorite) { showDialog.value = true }
            } else {
                EmptyState()
            }
        }
    }
    if (showDialog.value) {
        ShowDialog(
            title = "Clear Favorites",
            text = "Do you really want to clear your favorites?",
            onDismissed = { showDialog.value = false },
            negativeButtonText = "No",
            positiveButtonText = "Yes",
            onNegativeButtonClicked = { showDialog.value = false },
            onPositiveButtonClicked = {
                showDialog.value = false
                viewModel.clearFavorites()
            },
        )
    }
}

@Composable
private fun ProfileCard(user: User) {
    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(0.8.dp, Gray600, CircleShape)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = user.fullName,
                style = TextStyle(color = Black800, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            )
        }
    }
}

@Composable
private fun EmptyState() {
    Text(
        text = "Mark your favorite characters and see them here!",
        style = TextStyle(Black800, 16.sp, FontWeight.Bold)
    )
    Image(
        painter = painterResource(id = R.drawable.search_holder),
        modifier = Modifier.fillMaxWidth(),
        contentDescription = ""
    )
}

@Composable
@ExperimentalMaterialApi
private fun ResultState(
    favorites: List<FavoriteCharacter>,
    onDismissed: (favorite: FavoriteCharacter) -> Unit,
    onClearClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Favorites",
            modifier = Modifier
                .padding(horizontal = 8.dp),
            style = TextStyle(Black800, 16.sp, FontWeight.Bold)
        )
        IconButton(onClick = onClearClicked) {
            Icon(imageVector = Icons.Filled.Delete, contentDescription = "", tint = Gray700)
        }
    }
    LazyColumn {
        items(items = favorites, key = { favorite -> favorite.id }) { favorite ->
            FavoriteCharacterCard(favorite, onDismissed)
        }
    }
}

@Composable
@ExperimentalMaterialApi
private fun FavoriteCharacterCard(
    favorite: FavoriteCharacter,
    onDismissed: (favorite: FavoriteCharacter) -> Unit
) {
    val dismissState = rememberDismissState()
    if (dismissState.isDismissed(EndToStart)) onDismissed(favorite)
    SwipeToDismiss(
        state = dismissState,
        directions = setOf(EndToStart),
        modifier = Modifier.padding(bottom = 3.dp),
        dismissThresholds = { FractionalThreshold(0.35f) },
        background = {
            val color by animateColorAsState(
                if (dismissState.targetValue == Default) Gray else Red
            )
            val scale by animateFloatAsState(
                if (dismissState.targetValue == Default) 1f else 1.2f
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Description",
                    modifier = Modifier.scale(scale)
                )
            }
        }
    ) {
        Surface {
            Row {
                Image(
                    painter = rememberImagePainter(favorite.image),
                    contentDescription = favorite.name,
                    modifier = Modifier
                        .height(130.dp)
                        .width(125.dp)
                )
                Column {
                    val statusTextColor = when(favorite.status) {
                        "Alive" -> Green
                        "Dead" -> Red
                        else -> Gray600
                    }
                    CharacterCardTitle(text = favorite.name)
                    CharacterCardSimpleText(text = favorite.status, color = statusTextColor)
                    CharacterCardSimpleText(text = "${favorite.species} - ${favorite.gender}")
                    CharacterCardSimpleText(text = favorite.location)
                    CharacterCardSimpleText(text = "Created at: ${favorite.created.slice(0..9)}")
                }
            }
        }
    }
}
