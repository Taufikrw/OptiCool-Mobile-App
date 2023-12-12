package com.app.opticool.data.di

import android.content.Context
import com.app.opticool.data.EyeglassRepository
import com.app.opticool.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): EyeglassRepository {
        val apiService = ApiConfig.getApiService()
        return EyeglassRepository.getInstance(apiService)
    }
}