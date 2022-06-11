package com.example.togutravelapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.togutravelapp.R
import com.example.togutravelapp.data.ObjectWisataResponseItem
import com.example.togutravelapp.databinding.ActivityDetailObjectBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DetailObjectActivity : AppCompatActivity(), OnMapReadyCallback{
    private lateinit var mMap : GoogleMap
    private lateinit var binding: ActivityDetailObjectBinding
    private lateinit var scrollView : ScrollView
    private lateinit var transparentImageView : ImageView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailObjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        val objectDetail = intent.getParcelableExtra<ObjectWisataResponseItem>(EXTRA_DETAIL_OBJECT) as ObjectWisataResponseItem
        scrollView = binding.objectActivityScrollview
        transparentImageView = binding.transparentImage
        transparentImageView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    scrollView.requestDisallowInterceptTouchEvent(true)
                    false
                }
                MotionEvent.ACTION_UP -> {
                    scrollView.requestDisallowInterceptTouchEvent(true)
                    false
                }
                MotionEvent.ACTION_MOVE -> {
                    scrollView.requestDisallowInterceptTouchEvent(true)
                    false
                }
                else -> true
            }
        }
        val mFragmentManager = supportFragmentManager
        val mMapsFragment = mFragmentManager.findFragmentById(R.id.obj_location) as SupportMapFragment
        mMapsFragment.getMapAsync(this)
        
        setupData(objectDetail)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        val result = intent.getParcelableExtra<ObjectWisataResponseItem>(EXTRA_DETAIL_OBJECT)
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        setMaplocation(result!!)
    }

    private fun setMaplocation(objectDetail: ObjectWisataResponseItem){
        val sydney = LatLng(objectDetail.latitude!!.toDouble(), objectDetail.longtitude!!.toDouble())
        mMap.addMarker(MarkerOptions().position(sydney).title(objectDetail.nama).snippet(objectDetail.lokasi))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,15f))
    }
    private fun setupData(objectDetail : ObjectWisataResponseItem){
        val result = intent.getParcelableExtra<ObjectWisataResponseItem>(EXTRA_DETAIL_OBJECT)
        binding.apply {
            tvTitle.text = result?.nama
            tvDesc.text = result?.deskripsi
            locationTitle.text = resources.getString(R.string.lokasi)
            tvLocation.text = result?.lokasi

        }
        if (objectDetail.urlFotoObjek.isNullOrEmpty() || objectDetail.urlFotoObjek == "")
            Glide.with(this)
                .load(R.drawable.ios_android_free)
                .placeholder(R.drawable.ic_baseline_error_24)
                .centerCrop()
                .into(binding.imgObject)
        else
            Glide.with(this)
                .load(objectDetail.urlFotoObjek)
                .placeholder(R.drawable.ic_baseline_error_24)
                .centerCrop()
                .into(binding.imgObject)
    }

  companion object {
        const val EXTRA_DETAIL_OBJECT = "object"
    }
}