package com.example.togutravelapp.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.togutravelapp.activity.api.ApiConfig
import com.example.togutravelapp.data.ObjectWisataResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QRCodeViewModel:ViewModel() {

    private val _objectWisata = MutableLiveData<ObjectWisataResponseItem>()
    val objectWisata: LiveData<ObjectWisataResponseItem> = _objectWisata

    fun getObjetWisata(search: String, id: String){
        val client = ApiConfig.getApiService().getObjek(search)
        client.enqueue(object : Callback<List<ObjectWisataResponseItem?>?>{
            override fun onResponse(
                call: Call<List<ObjectWisataResponseItem?>?>,
                response: Response<List<ObjectWisataResponseItem?>?>
            ) {
                if (response.isSuccessful){
                    _objectWisata.value = response.body()!![id.toInt()]
                }else{
                    Log.d(TAG,"Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ObjectWisataResponseItem?>?>, t: Throwable) {
                Log.d(TAG,"onFailure: ${t.message.toString()}")
            }
        })
    }

}