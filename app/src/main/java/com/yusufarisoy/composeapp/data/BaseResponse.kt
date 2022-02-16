package com.yusufarisoy.composeapp.data

import com.google.gson.annotations.SerializedName

data class BaseResponse<out T : Any>(
    @SerializedName("info") val info: Info,
    @SerializedName("results") val data: T
)
