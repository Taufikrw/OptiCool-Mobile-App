package com.app.opticool.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.opticool.data.EyeglassRepository
import com.app.opticool.ui.common.EyeglassesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: EyeglassRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<EyeglassesState> = MutableStateFlow(EyeglassesState.Loading)
    val uiState: StateFlow<EyeglassesState>
        get() = _uiState

    fun getRecommendation() {
        viewModelScope.launch {
            _uiState.value = EyeglassesState.Loading
            repository.getAllEyeglasses()
                .catch {
                    _uiState.value = EyeglassesState.Error(it.message.toString())
                }.collect {
                    _uiState.value = EyeglassesState.Success(it!!.filter { item ->
                        item.faceShape == "heart"
                    }.take(5))
                }
        }
    }
}