package com.example.togutravelapp.data

import com.google.gson.annotations.SerializedName

data class ResponseLogin(

	@field:SerializedName("login")
	val login: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
