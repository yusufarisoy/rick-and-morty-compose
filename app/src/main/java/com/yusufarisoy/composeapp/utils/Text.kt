package com.yusufarisoy.composeapp.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusufarisoy.composeapp.ui.theme.Black800

@Composable
fun CharacterCardTitle(text: String) {
    Text(
        text = text,
        maxLines = 1,
        style = TextStyle(Black800, 16.sp, FontWeight.Bold),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
fun CharacterCardSimpleText(text: String, color: Color = Color.Black) {
    Text(
        text = text,
        maxLines = 1,
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    )
}
