package com.app.opticool.ui.common

import com.app.opticool.data.response.EyeglassesResponseItem

sealed interface EyeglassesState {
    data class Success(val eyeglass: List<EyeglassesResponseItem>): EyeglassesState
    data class Error(val error: String): EyeglassesState
    object Loading: EyeglassesState
}