package com.app.opticool.ui.common

import com.app.opticool.data.response.LoginResponse

sealed interface LoginState {
    data class Success(val user: LoginResponse): LoginState
    object Error: LoginState
    object Loading: LoginState
}