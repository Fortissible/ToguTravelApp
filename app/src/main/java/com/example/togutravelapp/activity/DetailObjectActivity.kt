package com.example.togutravelapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.togutravelapp.R
import com.example.togutravelapp.data.DetailObjModel
import com.example.togutravelapp.data.RemoteDataResource
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailObjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val btnBack = binding.btnBack
        btnBack.setOnClickListener {
            val intent = Intent(this, QRCodeScannerActivity ::class.java)
            startActivity(intent)
            finish()
        }

        val imgObject : ImageView = binding.imgObject
        Glide.with(this)
            .load("https://image-cdn.medkomtek.com/Kza0MUz3wf3-gMBOjEAOspGwWyc=/1200x675/smart/klikdokter-media-buckets/medias/2235774/original/000349600_1527968024-Jaga-Diri-Anda-dengan-5-Benda-Pelindung-Kesehatan-Ini-By-jakkapan-shutterstock.jpg")
            .into(imgObject)

        val mFragmentManager = supportFragmentManager
        val mMapsFragment = mFragmentManager.findFragmentById(R.id.obj_location) as SupportMapFragment
        mMapsFragment.getMapAsync(this)

        setupData()
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Sydney Park").snippet("Kec.Sukapinus"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,15f))

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }
    private fun setupData(){
        val repository = RemoteDataResource(this)
        val product = repository.getDetailObject().apply {
            binding.apply {
                tvTitle.text = title
                tvDesc.text = desc
                locationTitle.text = locTitle
                tvLocation.text = loc
            }
        }
        setupAccessibility(product)
    }

    private fun setupAccessibility(detailObjModel: DetailObjModel) {


        detailObjModel.apply {
            binding.apply {
                tvTitle.contentDescription = resources.getString(R.string.title)
                tvDesc.contentDescription = tvDesc.text
                locationTitle.contentDescription = resources.getString((R.string.lokasi))
                tvLocation.contentDescription = tvLocation.text
            }
        }
    }
}