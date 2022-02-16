package com.yusufarisoy.composeapp.domain

import com.yusufarisoy.composeapp.domain.home.LocationsUiModelMapper
import com.yusufarisoy.composeapp.domain.home.LocationsUiModelMapper.LocationsUiModel
import com.yusufarisoy.composeapp.network.RickAndMortyService
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val service: RickAndMortyService,
    private val mapper: LocationsUiModelMapper
) {

    suspend fun getLocations(
        locationName: String? = null,
        locationType: String? = null,
        locationDimension: String? = null,
        page: Int? = null
    ): LocationsUiModel {
        val response = service.getLocations(locationName, locationType, locationDimension, page)
        return mapper.map(response)
    }
}
