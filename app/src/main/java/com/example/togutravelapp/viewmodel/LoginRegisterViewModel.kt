package com.example.togutravelapp.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.togutravelapp.activity.api.ApiConfig
import com.example.togutravelapp.data.RegisterForm
import com.example.togutravelapp.data.ResponseRegister
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRegisterViewModel:ViewModel() {
    private val _registerError = MutableLiveData<String>()
    var registerError : LiveData<String> = _registerError

    fun registerUser(data: RegisterForm){
        val client = ApiConfig.getApiService().registerUser(data)
        client.enqueue(object : Callback<ResponseRegister>{
            override fun onResponse(call: Call<ResponseRegister>, response: Response<ResponseRegister>) {
                if (response.isSuccessful){
                    _registerError.value = response.body()?.error.toString()
                    Log.d(ContentValues.TAG,"onSuccess: ${response.body()}")
                }else{
                    _registerError.value = response.body()?.error.toString()
                    Log.d(ContentValues.TAG,"onFailure: ${response.body()}")
                }
            }
            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                Log.d(ContentValues.TAG,"onFailure: ${t.message.toString()}")
            }
        })
    }
}