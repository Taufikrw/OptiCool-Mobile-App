package com.app.opticool.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.opticool.data.EyeglassRepository
import com.app.opticool.ui.common.EyeglassState
import com.app.opticool.ui.common.EyeglassesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: EyeglassRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<EyeglassState> = MutableStateFlow(EyeglassState.Loading)
    val uiState: StateFlow<EyeglassState>
        get() = _uiState

    fun getDetail(id: Int) {
        viewModelScope.launch {
            _uiState.value = EyeglassState.Loading
            repository.getDetail(id)
                .catch {
                    _uiState.value = EyeglassState.Error
                }.collect {
                    _uiState.value = EyeglassState.Success(it!!)
                }
        }
    }
}