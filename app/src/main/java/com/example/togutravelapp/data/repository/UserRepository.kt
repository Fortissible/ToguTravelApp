package com.example.togutravelapp.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.example.togutravelapp.data.ResponseGetUserInfoItem
import com.example.togutravelapp.data.local.LoginSessionPreferences
import com.google.firebase.storage.FirebaseStorage

class UserRepository(val context: Context) {
    private val prefs = LoginSessionPreferences(context)

    fun setUserLoginInfoSession(userInfo : ResponseGetUserInfoItem, token: String) : String{
        val ref = FirebaseStorage.getInstance().reference.child("profile_images/${userInfo.email}")
        ref.downloadUrl.addOnSuccessListener {
            prefs.setUserProfileSession(userInfo,token,it)
            Log.d("SUKSESSSSS AMBIL IMG", "$it")
        }.addOnFailureListener {
            prefs.setUserProfileSession(userInfo,token,"".toUri())
            Log.d("GAGALLLL AMBIL IMG", "$it")
        }
        return token
    }

    fun getUserLoginInfoSession() : ResponseGetUserInfoItem{
        return prefs.getLoginProfile()
    }

    fun getUserTokenSession() : String{
        return prefs.getLoginSessionToken()
    }

    fun getUserProfileImage(): Uri {
        return prefs.getUserProfileImage()
    }

    fun clearInfoSession() {
        prefs.clearUserProfileSession()
    }
}