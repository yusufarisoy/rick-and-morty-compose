package com.yusufarisoy.composeapp.utils

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yusufarisoy.composeapp.ui.theme.Red300

@Composable
fun ShowDialog(
    title: String,
    text: String,
    onDismissed: () -> Unit,
    negativeButtonText: String,
    positiveButtonText: String,
    negativeButtonColor: Color = Color.Gray,
    positiveButtonColor: Color = Red300,
    onNegativeButtonClicked: () -> Unit,
    onPositiveButtonClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissed() },
        title = { Text(text = title, fontWeight = FontWeight.Bold) },
        text = { Text(text = text) },
        buttons = {
            Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                Button(
                    onClick = { onNegativeButtonClicked() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = negativeButtonColor,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth(.5f).padding(4.dp)
                ) {
                    Text(text = negativeButtonText)
                }
                Button(
                    onClick = { onPositiveButtonClicked() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = positiveButtonColor,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().padding(4.dp)
                ) {
                    Text(text = positiveButtonText)
                }
            }
        }
    )
}