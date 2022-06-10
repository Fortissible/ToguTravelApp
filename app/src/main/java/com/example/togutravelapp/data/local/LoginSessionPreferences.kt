package com.example.togutravelapp.data.local

import android.content.Context
import android.net.Uri
import androidx.core.content.edit
import androidx.core.net.toUri
import com.example.togutravelapp.data.ResponseGetUserInfoItem


class LoginSessionPreferences(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUserProfileSession(userInfo : ResponseGetUserInfoItem, token: String, imageUri : Uri){
        preferences.edit {
            putString(PREFS_USER_NAME,userInfo.nama)
            putString(PREFS_USER_EMAIL,userInfo.email)
            putString(PREFS_USER_TELP,userInfo.notelp)
            putString(PREFS_USER_UNAME,userInfo.username)
            putString(PREFS_USER_GENDER,userInfo.gender)
            putString(PREFS_USER_ROLE,userInfo.role)
            putString(PREFS_USER_PRICE,userInfo.harga.toString())
            putString(PREFS_USER_IMAGE, imageUri.toString())
            putString(PREFS_TOKEN, token)
        }
    }

    fun getLoginProfile(): ResponseGetUserInfoItem{
        return ResponseGetUserInfoItem(
            notelp = preferences.getString(PREFS_USER_TELP, EMPTY),
            nama = preferences.getString(PREFS_USER_NAME, EMPTY),
            username = preferences.getString(PREFS_USER_UNAME, EMPTY),
            role = preferences.getString(PREFS_USER_ROLE,"user"),
            harga = preferences.getString(PREFS_USER_PRICE,null),
            email = preferences.getString(PREFS_USER_EMAIL, EMPTY),
            gender = preferences.getString(PREFS_USER_GENDER, EMPTY)
        )
    }

    fun getLoginSessionToken() : String{
        return preferences.getString(PREFS_TOKEN, EMPTY).toString()
    }

    fun getUserProfileImage(): Uri {
        return preferences.getString(PREFS_USER_IMAGE,"")!!.toUri()
    }

    fun clearUserProfileSession(){
        preferences.edit{
            clear()
            apply()
        }
    }

    companion object {
        const val PREFS_NAME = "login_prefs"
        private const val PREFS_TOKEN = "login_token"
        private const val PREFS_USER_NAME = "login_name"
        private const val PREFS_USER_EMAIL = "login_email"
        private const val PREFS_USER_TELP = "login_telp"
        private const val PREFS_USER_UNAME = "login_uname"
        private const val PREFS_USER_GENDER = "login_gender"
        private const val PREFS_USER_ROLE = "login_role"
        private const val PREFS_USER_PRICE = "login_price"
        private const val PREFS_USER_IMAGE = "login_image"
        private const val EMPTY = ""
    }
}