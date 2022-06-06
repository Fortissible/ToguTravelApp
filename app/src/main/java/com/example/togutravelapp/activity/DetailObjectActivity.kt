package com.example.togutravelapp.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.model.UriLoader
import com.bumptech.glide.request.RequestListener
import com.example.togutravelapp.R
import com.example.togutravelapp.data.DetailObjModel
import com.example.togutravelapp.data.DummyObjectData
import com.example.togutravelapp.data.DummyRecommendData
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
    private lateinit var objectTitle: TextView
    private lateinit var objectDesc: TextView
    private lateinit var btnBack : ImageButton
  
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailObjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val objectDetail = intent.getParcelableExtra<DummyObjectData>(EXTRA_DETAIL_OBJECT) as DummyObjectData
        btnBack = binding.btnBack
        btnBack.setOnClickListener {
            val intent = Intent(this, QRCodeScannerActivity ::class.java)
            startActivity(intent)
            finish()
        }
        val imgObject : ImageView = binding.imgObject
        Glide.with(this)
            .load(objectDetail.objectUrl.toString())
            .placeholder(R.drawable.ic_baseline_error_24)
            .centerCrop()
            .into(imgObject)

        val mFragmentManager = supportFragmentManager
        val mMapsFragment = mFragmentManager.findFragmentById(R.id.obj_location) as SupportMapFragment
        mMapsFragment.getMapAsync(this)

        setupData(objectDetail)
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
    private fun setupData(objectDetail : DummyObjectData){
        val repository = RemoteDataResource(this)
        val product = repository.getDetailObject().apply {
            binding.apply {
                tvTitle.text = objectDetail.objectTitle ?: title
                tvDesc.text = objectDetail.objectDesc ?: desc
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
  companion object {
        const val EXTRA_DETAIL_OBJECT = "object"
    }
}