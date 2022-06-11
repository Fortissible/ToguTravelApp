package com.example.togutravelapp.data

import com.google.gson.annotations.SerializedName

data class ResponseFindTourguides(

	@field:SerializedName("Response")
	val response: List<TourguideItem>? = null
)

data class TourguideItem(

	@field:SerializedName("notelp")
	val notelp: String? = null,

	@field:SerializedName("iduser")
	val iduser: Int? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("harga")
	val harga: Int? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("url_image")
	val urlImage: Any? = null
)
