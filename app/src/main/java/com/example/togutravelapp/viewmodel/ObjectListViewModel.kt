package com.example.togutravelapp.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.togutravelapp.activity.api.ApiConfig
import com.example.togutravelapp.data.ObjectWisataResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ObjectListViewModel:ViewModel() {
    private val _objectWisata = MutableLiveData<List<ObjectWisataResponseItem>>()
    val objectWisata: LiveData<List<ObjectWisataResponseItem>> = _objectWisata

    fun getObjetWisata(){
        val client = ApiConfig.getApiService().getObjek("museum")
        client.enqueue(object : Callback<List<ObjectWisataResponseItem>> {
            override fun onResponse(
                call: Call<List<ObjectWisataResponseItem>>,
                response: Response<List<ObjectWisataResponseItem>>
            ) {
                if (response.isSuccessful){
                    _objectWisata.value = response.body()
                }else{
                    Log.d(ContentValues.TAG,"Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ObjectWisataResponseItem>>, t: Throwable) {
                Log.d(ContentValues.TAG,"onFailure: ${t.message.toString()}")
            }
        })
    }
}