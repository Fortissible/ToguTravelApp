package com.example.togutravelapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DummyRecommendData(
    val recomUrl : String? = null,
    val recomTitle : String? = null,
    val recomDesc : String? = null,
    val recomLoc : String? = null,
    val recomPrice : String? = null
):Parcelable
