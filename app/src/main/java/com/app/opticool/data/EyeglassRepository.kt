package com.app.opticool.data

import android.util.Log
import com.app.opticool.data.response.EyeglassesResponseItem
import com.app.opticool.data.response.LoginResponse
import com.app.opticool.data.retrofit.ApiService

class EyeglassRepository(
    private val apiService: ApiService
) {
    suspend fun getEyeglasses(): List<EyeglassesResponseItem> {
        return apiService.getEyeglasses()
    }

    suspend fun getDetail(id: Int): EyeglassesResponseItem {
        return apiService.getDetailEyeglass(id)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        Log.d("TEST MASUK", "MASUK REPO")
        return apiService.login(email, password)
    }

    companion object {
        @Volatile
        private var instance: EyeglassRepository? = null

        fun clearInstance() {
            instance = null
        }

        fun getInstance(
            apiService: ApiService
        ): EyeglassRepository =
            instance ?: synchronized(this) {
                instance ?: EyeglassRepository(apiService)
            }.also { instance = it }
    }
}