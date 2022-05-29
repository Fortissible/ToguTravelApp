package com.example.togutravelapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserListData(
    val name : String? = null
):Parcelable
