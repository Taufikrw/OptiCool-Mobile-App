package com.app.opticool.data.response

import com.google.gson.annotations.SerializedName

data class EyeglassesResponse(

	@field:SerializedName("EyeglassesResponse")
	val eyeglassesResponse: List<EyeglassesResponseItem>
)

data class EyeglassesResponseItem(

	@field:SerializedName("LinkPic2")
	val linkPic2: String,

	@field:SerializedName("LinkPic3")
	val linkPic3: String,

	@field:SerializedName("LinkPic1")
	val linkPic1: String,

	@field:SerializedName("FaceShape")
	val faceShape: String,

	@field:SerializedName("Gender")
	val gender: String,

	@field:SerializedName("FrameStyle")
	val frameStyle: String,

	@field:SerializedName("Name")
	val name: String,

	@field:SerializedName("Brand")
	val brand: String,

	@field:SerializedName("idEyeglass")
	val idEyeglass: Int,

	@field:SerializedName("Price")
	val price: String,

	@field:SerializedName("FrameShape")
	val frameShape: String,

	@field:SerializedName("FrameColour")
	val frameColour: String,

	@field:SerializedName("Link")
	val link: String
)
