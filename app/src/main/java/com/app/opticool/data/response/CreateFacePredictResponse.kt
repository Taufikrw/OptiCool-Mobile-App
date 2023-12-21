package com.app.opticool.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateFacePredictResponse(
    @field:SerializedName("prediction")
    val prediction: String
) : Parcelable