package com.example.togutravelapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageData(
    val msg: String? = null,
    val name: String? = null,
    val uid: String? = null,
    val profileUrl: String? = null,
    val timestamp: Long? = null
): Parcelable
