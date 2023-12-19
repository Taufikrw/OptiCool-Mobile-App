package com.app.opticool.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("token")
	val token: String
)

data class Data(

	@field:SerializedName("fullName")
	val fullName: String,

	@field:SerializedName("id")
	val id: Int
)
