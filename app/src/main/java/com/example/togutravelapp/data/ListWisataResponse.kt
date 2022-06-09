package com.example.togutravelapp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ListWisataResponse(

	@field:SerializedName("ListWisataResponse")
	val listWisataResponse: List<ListWisataResponseItem>
)

@Parcelize
data class ListWisataResponseItem(

	@field:SerializedName("keterangan")
	val keterangan: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("lokasi")
	val lokasi: String,

	@field:SerializedName("latitude")
	val latitude: String,

	@field:SerializedName("jenis")
	val jenis: String,

	@field:SerializedName("rating")
	val rating: Double,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("longitude")
	val longitude: String,

	@field:SerializedName("url_image")
	val urlImage: String
):Parcelable
