package com.example.togutravelapp.activity.api

import com.example.togutravelapp.data.ObjectWisataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("getobjek")
    suspend fun getObjek(
        @Header("nama") nama: String,
        @Query("lokasi") lokasi: String,
        @Query("id") id: String,
        @Query("deskripsi") deskripsi : String
    ): Call<ObjectWisataResponse>
}