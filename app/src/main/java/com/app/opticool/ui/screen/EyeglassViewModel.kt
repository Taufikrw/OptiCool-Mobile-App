package com.app.opticool.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.opticool.data.EyeglassRepository
import com.app.opticool.ui.common.EyeglassState
import com.app.opticool.ui.common.EyeglassesState
import kotlinx.coroutines.launch
import retrofit2.HttpException

class EyeglassViewModel(
    private val repository: EyeglassRepository
): ViewModel() {
    var eyeglassesState: EyeglassesState by mutableStateOf(EyeglassesState.Loading)
        private set

    var detailState: EyeglassState by mutableStateOf(EyeglassState.Loading)
        private set

    private val _query = mutableStateOf("")
    val query: androidx.compose.runtime.State<String> get() = _query

    fun getEyeglasses() {
        viewModelScope.launch {
            eyeglassesState = try {
                val result = repository.getEyeglasses()
                EyeglassesState.Success(result)
            } catch (e: HttpException) {
                EyeglassesState.Error(e.message.toString())
            }
        }
    }

    fun getRecommendation() {
        viewModelScope.launch {
            eyeglassesState = try {
                val result = repository.getEyeglasses().filter {
                    it.faceShape == "heart"
                }.take(5)
                EyeglassesState.Success(result)
            } catch (e: HttpException) {
                EyeglassesState.Error(e.message.toString())
            }
        }
    }

    fun getDetail(id: Int) {
        viewModelScope.launch {
            detailState = try {
                val result = repository.getDetail(id)
                EyeglassState.Success(result)
            } catch (e: Exception) {
                EyeglassState.Error
            } catch (e: HttpException) {
                EyeglassState.Error
            }
        }
    }

    fun search(query: String) {
        _query.value = query
        viewModelScope.launch {
            eyeglassesState = try {
                val result = repository.getEyeglasses().filter {
                    it.name.contains(_query.value, ignoreCase = true)
                }
                EyeglassesState.Success(result)
            } catch (e: HttpException) {
                EyeglassesState.Error(e.message.toString())
            }
        }
    }
}