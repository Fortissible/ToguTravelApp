package com.example.togutravelapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DummyTourGuideData(
    val tgUrl: String? = null,
    val tgName: String? = null,
    val tgGender: String? = null,
    val tgRating: String? = null,
    val tgPrice: String? = null,
    val tgUid: String? = null
) :Parcelable
