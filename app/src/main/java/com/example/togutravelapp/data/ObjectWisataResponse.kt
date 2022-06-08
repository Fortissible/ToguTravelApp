package com.example.togutravelapp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
data class ObjectWisataResponse(

	@field:SerializedName("ObjectWisataResponse")
	val objectWisataResponse: List<ObjectWisataResponseItem>
)
@Parcelize
data class ObjectWisataResponseItem(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("lokasi")
	val lokasi: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("deskripsi")
	val deskripsi: String
):Parcelable
