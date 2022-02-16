package com.yusufarisoy.composeapp.network

import com.yusufarisoy.composeapp.data.Character
import com.yusufarisoy.composeapp.data.Location
import com.yusufarisoy.composeapp.network.EndPoints.GET_CHARACTERS
import com.yusufarisoy.composeapp.network.EndPoints.GET_CHARACTER_BY_ID
import com.yusufarisoy.composeapp.network.EndPoints.GET_LOCATIONS
import com.yusufarisoy.composeapp.network.EndPoints.GET_LOCATION_BY_ID
import com.yusufarisoy.composeapp.data.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyService {

    @GET(GET_CHARACTERS)
    suspend fun getCharacters(
        @Query("name") characterName: String?,
        @Query("status") characterStatus: String?,
        @Query("gender") characterGender: String?,
        @Query("page") page: Int?
    ): BaseResponse<List<Character>>

    @GET(GET_CHARACTER_BY_ID)
    suspend fun getCharacterById(@Path("id") id: Int): Character

    @GET(GET_LOCATIONS)
    suspend fun getLocations(
        @Query("name") locationName: String?,
        @Query("type") locationType: String?,
        @Query("dimension") locationDimension: String?,
        @Query("page") page: Int?
    ): BaseResponse<List<Location>>

    @GET(GET_LOCATION_BY_ID)
    suspend fun getLocationById(@Path("id") id: Int): BaseResponse<Location>
}

object EndPoints {
    const val BASE_URL = "https://rickandmortyapi.com/api/"
    const val GET_CHARACTERS = "character"
    const val GET_CHARACTER_BY_ID = "character/{id}"
    const val GET_LOCATIONS = "location"
    const val GET_LOCATION_BY_ID = "location/{id}"
}
