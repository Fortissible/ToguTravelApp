package com.example.togutravelapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass){
            ChatListViewModel::class.java -> ChatListViewModel()
            LoginRegisterViewModel::class.java -> LoginRegisterViewModel()
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        } as T


    companion object {
        @Volatile
        private var instance : ViewModelFactory?= null
        fun getInstance(): ViewModelFactory =
            instance ?: synchronized(this){
                instance ?: ViewModelFactory()
            }.also { instance = it}
    }
}