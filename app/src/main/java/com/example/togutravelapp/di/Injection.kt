package com.example.togutravelapp.di

import android.content.Context
import com.example.togutravelapp.data.repository.UserRepository

object Injection {
    fun provideUserRepository(context: Context) : UserRepository{
        return UserRepository(context)
    }
}