package com.example.androidintermediate_sub1_wildanfajrialfarabi.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.togutravelapp.viewmodel.ChatListViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatListViewModel::class.java)){
            return ChatListViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance : ViewModelFactory ?= null
        fun getInstance(context: Context) : ViewModelFactory=
            instance?: synchronized(this){
                instance?: ViewModelFactory()
            }.also { instance = it}
    }
}