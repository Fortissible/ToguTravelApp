package com.example.togutravelapp.activity.api

import com.example.togutravelapp.data.ObjectWisataResponse
import com.example.togutravelapp.data.RegisterForm
import com.example.togutravelapp.data.ResponseRegister
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("getobjek")
    fun getObjek(
        @Query("search") search: String,
    ): Call<ObjectWisataResponse>

    @Headers("Content-Type: application/json")
    @POST("register")
    fun registerUser(
        @Body requestBody: RegisterForm
    ): Call<ResponseRegister>
}