package com.yusufarisoy.composeapp.data

data class User(
    val id: String,
    val name: String,
    val surname: String,
    val imageUrl: String
) {
    val fullName: String
        get() = "$name $surname"
}
