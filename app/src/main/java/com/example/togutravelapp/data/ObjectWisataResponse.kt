package com.example.togutravelapp.data

import com.google.gson.annotations.SerializedName

data class ObjectWisataResponse(

	@field:SerializedName("ObjectWisataResponse")
	val objectWisataResponse: List<ObjectWisataResponseItem>
)

data class ObjectWisataResponseItem(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("lokasi")
	val lokasi: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("deskripsi")
	val deskripsi: String
)
