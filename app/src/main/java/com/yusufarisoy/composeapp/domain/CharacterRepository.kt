package com.yusufarisoy.composeapp.domain

import com.yusufarisoy.composeapp.domain.characterdetail.CharacterDetailUiModelMapper
import com.yusufarisoy.composeapp.domain.characterdetail.CharacterDetailUiModelMapper.CharacterDetailUiModel
import com.yusufarisoy.composeapp.domain.home.CharactersUiModelMapper
import com.yusufarisoy.composeapp.domain.home.CharactersUiModelMapper.CharactersUiModel
import com.yusufarisoy.composeapp.network.RickAndMortyService
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val service: RickAndMortyService,
    private val charactersMapper: CharactersUiModelMapper,
    private val characterDetailMapper: CharacterDetailUiModelMapper
) {

    suspend fun getCharacters(
        characterName: String? = null,
        characterStatus: String? = null,
        characterGender: String? = null,
        page: Int? = null
    ): CharactersUiModel {
        val response = service.getCharacters(
            characterName,
            characterStatus,
            characterGender,
            page
        )
        return charactersMapper.map(response)
    }

    suspend fun getCharacterById(id: Int): CharacterDetailUiModel {
        val response = service.getCharacterById(id)
        return characterDetailMapper.map(response)
    }
}
