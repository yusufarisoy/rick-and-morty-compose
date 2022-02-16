package com.yusufarisoy.composeapp.domain.home

import com.yusufarisoy.composeapp.data.BaseResponse
import com.yusufarisoy.composeapp.data.Location
import com.yusufarisoy.composeapp.domain.home.LocationsUiModelMapper.LocationsUiModel
import com.yusufarisoy.composeapp.utils.Mapper
import javax.inject.Inject

class LocationsUiModelMapper @Inject constructor() : Mapper<BaseResponse<List<Location>>, LocationsUiModel> {

    override fun map(input: BaseResponse<List<Location>>): LocationsUiModel = with(input) {
        LocationsUiModel(
            locationCount = info.count,
            locations = data,
            nextPage = info.next
        )
    }

    data class LocationsUiModel(
        val locationCount: Int,
        val locations: List<Location>,
        val nextPage: String
    )
}
