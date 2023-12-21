package com.app.opticool.data.retrofit

import com.app.opticool.data.model.Login
import com.app.opticool.data.model.Register
import com.app.opticool.data.response.CreateFacePredictResponse
import com.app.opticool.data.response.EyeglassesResponseItem
import com.app.opticool.data.response.LoginResponse
import com.app.opticool.data.response.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @GET("eyeglass")
    suspend fun getEyeglasses(): List<EyeglassesResponseItem>

    @GET("eyeglass/{id}")
    suspend fun getDetailEyeglass(
        @Path("id")
        id: Int
    ): EyeglassesResponseItem

    @POST("login")
    suspend fun login(
        @Body user: Login
    ): LoginResponse

    @POST("register")
    suspend fun register(
        @Body user: Register
    ): RegisterResponse

    @Multipart
    @POST("predict")
    fun createFacePredict(
        @Part image: MultipartBody.Part
    ): Call<CreateFacePredictResponse>
}