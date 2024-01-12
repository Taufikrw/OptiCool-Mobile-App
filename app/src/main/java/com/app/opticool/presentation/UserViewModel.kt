package com.app.opticool.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.opticool.data.EyeglassRepository
import com.app.opticool.data.model.Login
import com.app.opticool.data.model.Register
import com.app.opticool.ui.common.LoginState
import com.app.opticool.ui.common.RegisterState
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UserViewModel(
    private val repository: EyeglassRepository
): ViewModel() {
    var userState: LoginState by mutableStateOf(LoginState.Loading)
        private set

    var newUserState: RegisterState by mutableStateOf(RegisterState.Loading)
        private set

    fun login(user: Login) {
        viewModelScope.launch {
            userState = try {
                val result = repository.login(user)
                LoginState.Success(result)
            } catch (e: Exception) {
                LoginState.Error
            } catch (e: HttpException) {
                LoginState.Error
            }
        }
    }

    suspend fun saveToken(token: String) = repository.saveToken(token)

    fun getToken(): LiveData<String> = repository.getSession().asLiveData()

    fun logout() {
        viewModelScope.launch {
            repository.destroyToken()
        }
    }

    fun register(user: Register) {
        viewModelScope.launch {
            newUserState = try {
                val result = repository.register(user)
                RegisterState.Success(result)
            } catch (e: Exception) {
                RegisterState.Error(e.message.toString())
            }
        }
    }
}