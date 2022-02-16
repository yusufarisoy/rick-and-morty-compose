package com.yusufarisoy.composeapp.domain.characterdetail

import com.yusufarisoy.composeapp.data.Character
import com.yusufarisoy.composeapp.domain.characterdetail.CharacterDetailUiModelMapper.CharacterDetailUiModel
import com.yusufarisoy.composeapp.utils.Mapper
import javax.inject.Inject

class CharacterDetailUiModelMapper @Inject constructor() : Mapper<Character, CharacterDetailUiModel> {

    override fun map(input: Character): CharacterDetailUiModel = CharacterDetailUiModel(input)

    data class CharacterDetailUiModel(
        val character: Character,
        var isFavorite: Boolean = false
    )
}
