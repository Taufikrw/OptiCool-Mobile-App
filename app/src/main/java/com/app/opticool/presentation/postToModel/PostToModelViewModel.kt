package com.app.opticool.presentation.postToModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.opticool.data.model.HttpResponseCode
import com.app.opticool.data.response.CreateFacePredictResponse
import com.app.opticool.data.retrofit.ApiConfig
import com.app.opticool.helper.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PostToModelViewModel: ViewModel(){
    private val _uiState: MutableStateFlow<UiState<CreateFacePredictResponse>> =
        MutableStateFlow(UiState.Idle)
    val uiState: StateFlow<UiState<CreateFacePredictResponse>> get() = _uiState

    private val _face = MutableLiveData<CreateFacePredictResponse>()
    val face: LiveData<CreateFacePredictResponse> = _face

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _responseType = MutableLiveData<Int>()
    val responseType: LiveData<Int> = _responseType

    fun uploadImage(photo: File) {
        _isLoading.value = true
        _responseType.value = HttpResponseCode.SUCCESS

        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), photo)
        val body = MultipartBody.Part.createFormData("image", photo.name, requestFile)

        val client = ApiConfig.getApiModel().createFacePredict(body)


        client.enqueue(object : Callback<CreateFacePredictResponse> {
            override fun onResponse(
                call: Call<CreateFacePredictResponse>,
                response: Response<CreateFacePredictResponse>
            ) {
                Log.d("POST TO VIEW MODEL", "onResponse: ${response.body()}")
                if (response.isSuccessful) {
                    val responseBody = response.body()?.prediction
                    if (responseBody != null ) {
                        _face.value = response.body()
                        _isLoading.value = false
                    } else {
                        _responseType.value = HttpResponseCode.FAILED
                        _isLoading.value = false
                    }
                } else {
                    _responseType.value = HttpResponseCode.FAILED
                    _isLoading.value = false
                    Log.d("POST TO VIEW MODEL", "onResponse: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<CreateFacePredictResponse>, t: Throwable){
                _responseType.value = HttpResponseCode.SERVER_ERROR
                _isLoading.value = false
                Log.d("POST TO VIEW MODEL", "onResponse: ${t.message}")
            }
        })
    }
}