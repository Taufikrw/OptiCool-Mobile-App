package com.app.opticool.data

import android.util.Log
import com.app.opticool.data.model.Login
import com.app.opticool.data.model.Register
import com.app.opticool.data.preferences.UserPreferences
import com.app.opticool.data.response.EyeglassesResponseItem
import com.app.opticool.data.response.LoginResponse
import com.app.opticool.data.response.RegisterResponse
import com.app.opticool.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EyeglassRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {
    suspend fun getAllEyeglasses(): Flow<List<EyeglassesResponseItem>?> {
        return try {
            val result = apiService.getEyeglasses()
            flowOf(result)
        } catch (t: Throwable) {
            flowOf(null)
        }
    }

    suspend fun getEyeglasses(): List<EyeglassesResponseItem> {
        return apiService.getEyeglasses()
    }

    suspend fun getDetail(id: Int): Flow<EyeglassesResponseItem?> {
        return try {
            val result = apiService.getDetailEyeglass(id)
            flowOf(result)
        } catch (t: Throwable) {
            flowOf(null)
        }
    }

    suspend fun login(user: Login): LoginResponse {
        return apiService.login(user)
    }

    suspend fun saveToken(token: String) {
        userPreferences.saveToken(token)
    }

    fun getSession(): Flow<String> = userPreferences.getLoginToken()

    suspend fun destroyToken() {
        userPreferences.destroyToken()
    }

    suspend fun register(user: Register): RegisterResponse {
        return apiService.register(user)
    }

    companion object {
        @Volatile
        private var instance: EyeglassRepository? = null

        fun clearInstance() {
            instance = null
        }

        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): EyeglassRepository =
            instance ?: synchronized(this) {
                instance ?: EyeglassRepository(apiService, userPreferences)
            }.also { instance = it }
    }
}