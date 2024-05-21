package com.capstone.dressify.response

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)
