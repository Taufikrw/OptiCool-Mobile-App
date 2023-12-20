package com.app.opticool.data.di

import android.content.Context
import android.util.Log
import com.app.opticool.data.EyeglassRepository
import com.app.opticool.data.preferences.UserPreferences
import com.app.opticool.data.preferences.dataStore
import com.app.opticool.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): EyeglassRepository = runBlocking {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = pref.getLoginToken().first()
        ApiConfig.setAuthToken(user)
        val apiService = ApiConfig.getApiService()
        EyeglassRepository.getInstance(apiService, pref)
    }
}