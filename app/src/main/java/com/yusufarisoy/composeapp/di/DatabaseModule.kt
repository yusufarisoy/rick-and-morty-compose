package com.yusufarisoy.composeapp.di

import android.content.Context
import androidx.room.Room
import com.yusufarisoy.composeapp.database.FavoriteCharacterDao
import com.yusufarisoy.composeapp.database.RickAndMortyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityRetainedComponent::class)
class DatabaseModule {

    @Provides
    fun provideFavoriteCharacterDao(database: RickAndMortyDatabase): FavoriteCharacterDao =
        database.favoriteCharacterDao()

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): RickAndMortyDatabase {
        return Room
            .databaseBuilder(context, RickAndMortyDatabase::class.java, "RickAndMortyDatabase")
            .build()
    }
}
