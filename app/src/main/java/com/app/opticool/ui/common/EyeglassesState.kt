package com.app.opticool.ui.common

import com.app.opticool.data.response.EyeglassesResponseItem

sealed interface EyeglassesState {
    data class Success(val eyeglass: List<EyeglassesResponseItem>): EyeglassesState
    object Error: EyeglassesState
    object Loading: EyeglassesState
}