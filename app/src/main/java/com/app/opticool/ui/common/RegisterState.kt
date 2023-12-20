package com.app.opticool.ui.common

import com.app.opticool.data.response.RegisterResponse

sealed interface RegisterState {
    data class Success(val user: RegisterResponse): RegisterState
    data class Error(val error: String): RegisterState
    object Loading: RegisterState
}