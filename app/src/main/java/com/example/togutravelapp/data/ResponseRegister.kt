package com.example.togutravelapp.data

import com.google.gson.annotations.SerializedName

data class ResponseRegister(
	@field:SerializedName("error")
	val error: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
