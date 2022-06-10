package com.example.togutravelapp.data

import com.google.gson.annotations.SerializedName

data class ResponseGetUserInfo(

	@field:SerializedName("ResponseGetUserInfo")
	val responseGetUserInfo: List<ResponseGetUserInfoItem?>? = null
)

data class ResponseGetUserInfoItem(

	@field:SerializedName("notelp")
	val notelp: String? = null,

	@field:SerializedName("iduser")
	val iduser: Int? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("harga")
	val harga: Any? = null,

	@field:SerializedName("rating")
	val rating: Any? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null
)
