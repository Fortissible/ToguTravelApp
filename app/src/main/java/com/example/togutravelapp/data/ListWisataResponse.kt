package com.example.togutravelapp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ListWisataResponse(

	@field:SerializedName("ListWisataResponse")
	val listWisataResponse: List<ListWisataResponseItem?>? = null
)

@Parcelize
data class ListWisataResponseItem(

	@field:SerializedName("keterangan")
	val keterangan: String?=null,

	@field:SerializedName("nama")
	val nama: String?=null,

	@field:SerializedName("harga")
	val harga: Int?=null,

	@field:SerializedName("lokasi")
	val lokasi: String?=null,

	@field:SerializedName("latitude")
	val latitude: String?=null,

	@field:SerializedName("jenis")
	val jenis: String?=null,

	@field:SerializedName("rating")
	val rating: Double?=null,

	@field:SerializedName("id")
	val id: Int?=null,

	@field:SerializedName("longitude")
	val longitude: String?=null,

	@field:SerializedName("url_image")
	val urlImage: String?=null
):Parcelable
