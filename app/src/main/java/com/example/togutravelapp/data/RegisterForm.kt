package com.example.togutravelapp.data

data class RegisterForm(
	val email: String,
	val password: String,
	val nama: String,
	val username: String,
	val notelp: String,
	val role: String,
	val url_image: String? = null,
	val gender: String
)

data class LoginForm(
	val email: String,
	val password: String
)

data class Token(
	val token: String
)
