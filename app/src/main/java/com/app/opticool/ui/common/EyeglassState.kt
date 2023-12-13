package com.app.opticool.ui.common

import com.app.opticool.data.response.EyeglassesResponseItem

sealed interface EyeglassState {
    data class Success(val eyeglass: EyeglassesResponseItem): EyeglassState
    object Error: EyeglassState
    object Loading: EyeglassState
}