package com.example.togutravelapp.data

import android.content.Context
import com.example.togutravelapp.R

class RemoteDataResource(private val context: Context){
    fun getDetailObject():DetailObjModel{
        return DetailObjModel(
            title = context.getString(R.string.title),
            desc = context.getString(R.string.descriptions),
            locTitle = context.getString(R.string.lokasi),
            loc = context.getString(R.string.alamat)
        )
    }
}