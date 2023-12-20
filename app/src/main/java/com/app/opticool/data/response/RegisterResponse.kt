package com.app.opticool.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("data")
	val data: DataRegister
)

data class DataRegister(

	@field:SerializedName("phoneNumber")
	val phoneNumber: String,

	@field:SerializedName("profilePic")
	val profilePic: String,

	@field:SerializedName("fullName")
	val fullName: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String
)
