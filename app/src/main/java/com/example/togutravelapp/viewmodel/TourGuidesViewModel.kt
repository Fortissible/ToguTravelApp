package com.example.togutravelapp.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.togutravelapp.activity.api.ApiConfig
import com.example.togutravelapp.data.TourguideItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TourGuidesViewModel: ViewModel() {
    private val _tourGuides = MutableLiveData<List<TourguideItem>?>()
    val tourGuides : LiveData<List<TourguideItem>?> = _tourGuides

    fun findTourGuides(query : String? = null){
        val client = ApiConfig.getApiService().findTourGuide(query)
        client.enqueue(object:Callback<List<TourguideItem>?>{
            override fun onResponse(
                call: Call<List<TourguideItem>?>,
                response: Response<List<TourguideItem>?>
            ) {
                if (response.isSuccessful){
                    _tourGuides.value = response.body()
                    Log.d(TAG, "Success: ${response.message()}")
                } else {
                    Log.d(TAG, "Error: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<TourguideItem>?>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}