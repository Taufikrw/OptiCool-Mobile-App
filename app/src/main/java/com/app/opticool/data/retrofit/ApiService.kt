package com.app.opticool.data.retrofit

import com.app.opticool.data.response.EyeglassesResponse
import com.app.opticool.data.response.EyeglassesResponseItem
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("eyeglass")
    suspend fun getEyeglasses(): List<EyeglassesResponseItem>

    @GET("eyeglass/{id}")
    suspend fun getDetailEyeglass(
        @Path("id")
        id: Int
    ): EyeglassesResponseItem
}