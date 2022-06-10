package com.example.togutravelapp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ObjectWisataResponse(

	@field:SerializedName("ObjectWisataResponse")
	val objectWisataResponse: List<ObjectWisataResponseItem?>? = null
)

@Parcelize
data class ObjectWisataResponseItem(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("lokasi")
	val lokasi: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("longtitude")
	val longtitude: String? = null,

	@field:SerializedName("url_foto_objek")
	val urlFotoObjek: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null

):Parcelable

