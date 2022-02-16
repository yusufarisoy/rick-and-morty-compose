package com.yusufarisoy.composeapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yusufarisoy.composeapp.data.local.FavoriteCharacter

@Database(entities = [FavoriteCharacter::class], version = 1)
abstract class RickAndMortyDatabase : RoomDatabase() {

    abstract fun favoriteCharacterDao(): FavoriteCharacterDao
}
