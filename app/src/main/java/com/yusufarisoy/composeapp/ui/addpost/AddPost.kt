package com.yusufarisoy.composeapp.ui.addpost

import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun AddPost(
    navController: NavController
) {
    TextField(
        value = "",
        onValueChange = {},
    )
    // TODO: Finish AddPost form
}
