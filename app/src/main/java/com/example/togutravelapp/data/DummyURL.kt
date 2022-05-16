package com.example.togutravelapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DummyURL(
    val urlImage : String? = null
): Parcelable
