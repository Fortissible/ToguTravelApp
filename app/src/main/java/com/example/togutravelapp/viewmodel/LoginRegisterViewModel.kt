package com.example.togutravelapp.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.togutravelapp.activity.api.ApiConfig
import com.example.togutravelapp.data.*
import com.example.togutravelapp.data.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRegisterViewModel(private val userRepository: UserRepository)
    :ViewModel() {
    private val _tokenSession = MutableLiveData<String>()
    var tokenSession : LiveData<String> = _tokenSession
    private val _isLoginInProgress = MutableLiveData<Boolean>()
    var isLoginInProgress : LiveData<Boolean> = _isLoginInProgress
    private val _registerError = MutableLiveData<String>()
    var registerError : LiveData<String> = _registerError

    fun registerUser(data: RegisterForm){
        val client = ApiConfig.getApiService().registerUser(data)
        client.enqueue(object : Callback<ResponseRegister>{
            override fun onResponse(call: Call<ResponseRegister>, response: Response<ResponseRegister>) {
                if (response.isSuccessful){
                    _registerError.value = response.body()?.error.toString()
                    Log.d(TAG,"onSuccess: ${response.body()}")
                }else{
                    _registerError.value = response.body()?.error.toString()
                    Log.d(TAG,"onFailure: ${response.body()}")
                }
            }
            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                Log.d(TAG,"onFailure: ${t.message.toString()}")
            }
        })
    }

    fun loginUser(data: LoginForm){
        _isLoginInProgress.value = true
        val client = ApiConfig.getApiService().loginUser(data)
        client.enqueue(object: Callback<ResponseLogin>{
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if (response.isSuccessful && response.body()!!.login.toString() == "sukses"){
                    val delimiter = "'"
                    val tokenPreprocessed = response.body()!!.token!!.split(delimiter)
                    setUserInfo(tokenPreprocessed[1])
                    Log.d(TAG,"onSuccess: ${response.body()}")
                }
                else{
                    Log.d(TAG,"onFailure: ${response.body()}")
                    _isLoginInProgress.value = false
                }
            }
            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message.toString()}")
                _isLoginInProgress.value = false
            }
        })
    }

    fun setUserInfo(token : String){
        val tokenForm = Token(token = token)
        val client = ApiConfig.getApiService().getInfoUser(tokenForm)
        client.enqueue(object : Callback<List<ResponseGetUserInfoItem>>{
            override fun onResponse(
                call: Call<List<ResponseGetUserInfoItem>>,
                response: Response<List<ResponseGetUserInfoItem>>
            ) {
                if (response.isSuccessful){
                    _tokenSession.value = userRepository.setUserLoginInfoSession(response.body()!![0], token)
                    Log.d(TAG, "onResponse: ${response.body().toString()}")
                    _isLoginInProgress.value = false
                }
                else{
                    Log.d(TAG, "onResponse: ${response.body().toString()}")
                    _isLoginInProgress.value = false
                }
            }
            override fun onFailure(call: Call<List<ResponseGetUserInfoItem>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message.toString()}")
                _isLoginInProgress.value = false
            }
        })
    }

    fun getUserTokenSession(){
        _tokenSession.value = userRepository.getUserTokenSession()
    }
}