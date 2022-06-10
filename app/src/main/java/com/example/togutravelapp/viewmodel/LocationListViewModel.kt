package com.example.togutravelapp.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.togutravelapp.activity.api.ApiConfig
import com.example.togutravelapp.data.ListWisataResponse
import com.example.togutravelapp.data.ListWisataResponseItem
import com.example.togutravelapp.data.ObjectWisataResponse
import com.example.togutravelapp.data.ObjectWisataResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationListViewModel:ViewModel() {
    private val _listWisata = MutableLiveData<List<ListWisataResponseItem>>()
    val listWisata: LiveData<List<ListWisataResponseItem>> = _listWisata

    fun getlistWisata(query : String? = null){
        val client = ApiConfig.getApiService().getWisata(query)
        client.enqueue(object : Callback<List<ListWisataResponseItem>> {
            override fun onResponse(
                call: Call<List<ListWisataResponseItem>>,
                response: Response<List<ListWisataResponseItem>>
            ) {
                if (response.isSuccessful){
                    _listWisata.value = response.body()
                }else{
                    Log.d(ContentValues.TAG,"Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ListWisataResponseItem>>, t: Throwable) {
                Log.d(ContentValues.TAG,"onFailure: ${t.message.toString()}")
            }
        })
    }
}