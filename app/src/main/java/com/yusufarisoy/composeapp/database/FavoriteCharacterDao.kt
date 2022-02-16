package com.yusufarisoy.composeapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.yusufarisoy.composeapp.data.local.FavoriteCharacter

@Dao
interface FavoriteCharacterDao {
    @Insert
    suspend fun add(character: FavoriteCharacter)

    @Query("SELECT * FROM Favorite_Character")
    suspend fun getFavorites(): List<FavoriteCharacter>

    @Query("SELECT * FROM Favorite_Character WHERE id = :id")
    suspend fun getById(id: Int): FavoriteCharacter?

    @Delete
    suspend fun delete(character: FavoriteCharacter)

    @Query("DELETE FROM Favorite_Character")
    suspend fun clear()
}
