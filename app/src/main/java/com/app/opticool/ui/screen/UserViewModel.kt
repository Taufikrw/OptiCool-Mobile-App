package com.app.opticool.ui.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.opticool.data.EyeglassRepository
import com.app.opticool.ui.common.LoginState
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UserViewModel(
    private val repository: EyeglassRepository
): ViewModel() {
    var userState: LoginState by mutableStateOf(LoginState.Loading)
        private set

    fun login(email: String, password: String) {
        Log.d("TEST MASUK", "MASUK VM")
        viewModelScope.launch {
            userState = try {
                val result = repository.login(email, password)
                Log.d("TEST MASUK", result.toString())
                LoginState.Success(result)
            } catch (e: Exception) {
                Log.d("TEST MASUK E", e.toString())
                LoginState.Error
            } catch (e: HttpException) {
                Log.d("TEST MASUK EH", e.toString())
                LoginState.Error
            }
        }
    }
}