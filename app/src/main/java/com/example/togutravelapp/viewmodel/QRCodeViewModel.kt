package com.example.togutravelapp.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.togutravelapp.activity.api.ApiConfig
import com.example.togutravelapp.data.ObjectWisataResponse
import com.example.togutravelapp.data.ObjectWisataResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QRCodeViewModel:ViewModel() {

    private val _objectWisata = MutableLiveData<List<ObjectWisataResponseItem>>()
    val objectWisata: LiveData<List<ObjectWisataResponseItem>> = _objectWisata

    fun getObjetWisata(search: String){
        val client = ApiConfig.getApiService().getObjek(search)
        client.enqueue(object : Callback<ObjectWisataResponse>{
            override fun onResponse(
                call: Call<ObjectWisataResponse>,
                response: Response<ObjectWisataResponse>
            ) {
                if (response.isSuccessful){
                    _objectWisata.value = response.body()?.objectWisataResponse
                }else{
                    Log.d(TAG,"Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ObjectWisataResponse>, t: Throwable) {
                Log.d(TAG,"onFailure: ${t.message.toString()}")
            }
        })
    }

}