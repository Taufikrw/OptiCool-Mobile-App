package com.app.opticool.ui.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.opticool.data.EyeglassRepository
import com.app.opticool.ui.common.EyeglassesState
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RecommendViewModel(
    private val repository: EyeglassRepository
): ViewModel() {
    var eyeglassesState: EyeglassesState by mutableStateOf(EyeglassesState.Loading)
        private set

    init {
        getEyeglasses()
    }

    fun getEyeglasses() {
        viewModelScope.launch {
            eyeglassesState = try {
                val result = repository.getEyeglasses().filter {
                    it.faceShape == "oval"
                }.take(5)
                EyeglassesState.Success(result)
            } catch (e: Exception) {
                EyeglassesState.Error
            } catch (e: HttpException) {
                EyeglassesState.Error
            }
        }
    }
}