package com.capstone.dressify.data.remote.response

import com.google.gson.annotations.SerializedName

data class CatalogResponse(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)
