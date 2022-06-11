package com.example.togutravelapp.activity.api


import com.example.togutravelapp.data.*
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @GET("getobjek")
    fun getObjek(
        @Query("search") search: String,
    ): Call<List<ObjectWisataResponseItem>>

    @Headers("Content-Type: application/json")
    @POST("register")
    fun registerUser(
        @Body requestBody: RegisterForm
    ): Call<ResponseRegister>

    @Headers("Content-Type: application/json")
    @POST("login")
    fun loginUser(
        @Body requestBody: LoginForm
    ): Call<ResponseLogin>

    @Headers("Content-Type: application/json")
    @POST("getinfouser")
    fun getInfoUser(
        @Body requestBody: Token
    ): Call<List<ResponseGetUserInfoItem>>

    @GET("getwisata")
    fun getWisata(
        @Query("search") search: String? = null,
    ):Call<List<ListWisataResponseItem>>

    @GET("findtourguide")
    fun findTourGuide(
        @Query("search") search: String? = null,
    ):Call<List<TourguideItem>?>
}