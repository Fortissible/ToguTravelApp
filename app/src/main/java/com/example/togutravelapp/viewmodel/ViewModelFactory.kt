package com.example.togutravelapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.togutravelapp.data.repository.UserRepository
import com.example.togutravelapp.di.Injection

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
    private val userRepository: UserRepository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass){
            ChatListViewModel::class.java -> ChatListViewModel()
            LoginRegisterViewModel::class.java -> LoginRegisterViewModel(userRepository)
            LocationListViewModel::class.java -> LocationListViewModel()
            ObjectListViewModel::class.java -> ObjectListViewModel()
            TourGuidesViewModel::class.java -> TourGuidesViewModel()
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        } as T


    companion object {
        @Volatile
        private var instance : ViewModelFactory?= null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this){
                instance ?: ViewModelFactory(
                    Injection.provideUserRepository(context = context)
                )
            }.also { instance = it}
    }
}