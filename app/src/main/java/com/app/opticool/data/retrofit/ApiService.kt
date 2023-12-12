package com.app.opticool.data.retrofit

import com.app.opticool.data.response.EyeglassesResponse
import com.app.opticool.data.response.EyeglassesResponseItem
import retrofit2.http.GET

interface ApiService {
    @GET("eyeglass")
    suspend fun getEyeglasses(): List<EyeglassesResponseItem>
}