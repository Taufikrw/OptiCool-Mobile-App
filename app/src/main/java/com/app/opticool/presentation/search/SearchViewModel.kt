package com.app.opticool.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.opticool.data.EyeglassRepository
import com.app.opticool.ui.common.EyeglassesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: EyeglassRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<EyeglassesState> = MutableStateFlow(EyeglassesState.Loading)
    val uiState: StateFlow<EyeglassesState>
        get() = _uiState
    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun getAllEyeglasses() {
        viewModelScope.launch {
            _uiState.value = EyeglassesState.Loading
            repository.getAllEyeglasses()
                .catch {
                    _uiState.value = EyeglassesState.Error(it.message.toString())
                }.collect {
                    _uiState.value = EyeglassesState.Success(it!!)
                }
        }
    }

    fun searchEyeglasses(query: String) {
        _query.value = query
        viewModelScope.launch {
            repository.getAllEyeglasses()
                .catch {
                    _uiState.value = EyeglassesState.Error(it.message.toString())
                }.collect {
                    _uiState.value = EyeglassesState.Success(it!!.filter { item ->
                        item.name.contains(_query.value, ignoreCase = true)
                    })
                }
        }
    }
}