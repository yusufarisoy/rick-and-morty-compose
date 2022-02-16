package com.yusufarisoy.composeapp.domain.home

import com.yusufarisoy.composeapp.data.BaseResponse
import com.yusufarisoy.composeapp.data.Character
import com.yusufarisoy.composeapp.domain.home.CharactersUiModelMapper.CharactersUiModel
import com.yusufarisoy.composeapp.utils.Mapper
import javax.inject.Inject

class CharactersUiModelMapper @Inject constructor() : Mapper<BaseResponse<List<Character>>, CharactersUiModel> {

    override fun map(input: BaseResponse<List<Character>>): CharactersUiModel = with(input) {
        CharactersUiModel(info.count, data, info.next)
    }

    data class CharactersUiModel(
        val characterCount: Int,
        val characters: List<Character>,
        val nextPage: String
    )
}
