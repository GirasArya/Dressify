package com.capstone.dressify.data.remote.response

import com.google.gson.annotations.SerializedName

data class CatalogResponse(

	@field:SerializedName("totalItems")
	val totalItems: Int? = null,

	@field:SerializedName("totalPages")
	val totalPages: Int? = null,

	@field:SerializedName("pageSize")
	val pageSize: Int? = null,

	@field:SerializedName("clothingItems")
	val clothingItems: List<ClothingItemsItem>,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("currentPage")
	val currentPage: Int? = null
)

data class ClothingItemsItem(

	@field:SerializedName("type_of_clothing")
	val typeOfClothing: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("color")
	val color: String? = null,

	@field:SerializedName("product_display_name")
	val productDisplayName: String? = null,

	@field:SerializedName("season")
	val season: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("usages")
	val usages: String? = null,

	@field:SerializedName("picture_link")
	val pictureLink: String? = null
)
