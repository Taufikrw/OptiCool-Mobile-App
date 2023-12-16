package com.app.opticool.data.retrofit

import com.app.opticool.data.response.EyeglassesResponse
import com.app.opticool.data.response.EyeglassesResponseItem
import com.app.opticool.data.response.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("eyeglass")
    suspend fun getEyeglasses(): List<EyeglassesResponseItem>

    @GET("eyeglass/{id}")
    suspend fun getDetailEyeglass(
        @Path("id")
        id: Int
    ): EyeglassesResponseItem

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse
}