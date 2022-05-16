package com.example.togutravelapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DummyObjectData(
    val objectUrl : String? = null,
    val objectTitle : String? = null,
    val objectDesc : String? = null
):Parcelable
