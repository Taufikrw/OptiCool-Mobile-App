package com.app.opticool.ui.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.opticool.data.EyeglassRepository
import com.app.opticool.data.model.Login
import com.app.opticool.ui.common.LoginState
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UserViewModel(
    private val repository: EyeglassRepository
): ViewModel() {
    var userState: LoginState by mutableStateOf(LoginState.Loading)
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
}