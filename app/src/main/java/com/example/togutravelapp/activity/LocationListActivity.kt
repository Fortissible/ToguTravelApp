package com.example.togutravelapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.togutravelapp.R
import com.example.togutravelapp.activity.fragment.LogoutFragment
import com.example.togutravelapp.adapter.ListRecommendationAdapter
import com.example.togutravelapp.data.DummyRecommendData
import com.example.togutravelapp.databinding.ActivityLocationListBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LocationListActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding : ActivityLocationListBinding
    private lateinit var locationRv : RecyclerView
    private lateinit var profilePic : ImageView
    private lateinit var mMap: GoogleMap
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profilePic = binding.locationListProfilepic
        auth = Firebase.auth

        Glide.with(this)
            .load(auth.currentUser!!.photoUrl)
            .centerCrop()
            .into(profilePic)

        locationRv = binding.locationListRv
        locationRv.setHasFixedSize(true)
        supportActionBar?.hide()

        profilePic.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentByTag(LogoutFragment::class.java.simpleName)
            if (fragment !is LogoutFragment) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.location_list_activity, LogoutFragment(), LogoutFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
        }

        setDummyListLocationData()

        val dropdownItemPrice = resources.getStringArray(R.array.sort_by_price)
        val dropdownItemRange = resources.getStringArray(R.array.sort_by_range)
        val arrayAdapterPrice = ArrayAdapter(this,R.layout.dropdown_item,dropdownItemPrice)
        binding.buttonDropdownSortPrice.setAdapter(arrayAdapterPrice)
        val arrayAdapterRange = ArrayAdapter(this,R.layout.dropdown_item,dropdownItemRange)
        binding.buttonDropdownSortRange.setAdapter(arrayAdapterRange)

        val mFragmentManager = supportFragmentManager
        val mMapsFragment = mFragmentManager.findFragmentById(R.id.map_location) as SupportMapFragment
        mMapsFragment.getMapAsync(this)
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

    override fun onRestart() {
        super.onRestart()
        val dropdownItemPrice = resources.getStringArray(R.array.sort_by_price)
        val dropdownItemRange = resources.getStringArray(R.array.sort_by_range)
        val arrayAdapterPrice = ArrayAdapter(this,R.layout.dropdown_item,dropdownItemPrice)
        binding.buttonDropdownSortPrice.setAdapter(arrayAdapterPrice)
        val arrayAdapterRange = ArrayAdapter(this,R.layout.dropdown_item,dropdownItemRange)
        binding.buttonDropdownSortRange.setAdapter(arrayAdapterRange)
    }

    private fun setDummyListLocationData(){
        val dummyListLocation = getDummyListLocationData()
        locationRv.layoutManager = LinearLayoutManager(this)
        val adapter = ListRecommendationAdapter(dummyListLocation)
        locationRv.adapter = adapter

        adapter.setOnItemClickCallback(object: ListRecommendationAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DummyRecommendData) {
                Log.d("Clicked Item", "$data")
                val intentToDetail = Intent(this@LocationListActivity, DetailLocationActivity::class.java)
                intentToDetail.putExtra(DetailLocationActivity.EXTRA_LOCATIONDETAIL, data)
                startActivity(intentToDetail)
            }
        })
    }

    private fun getDummyListLocationData(): List<DummyRecommendData> {
        val recomTitle = resources.getStringArray(R.array.recommend_title)
        val recomDesc = resources.getStringArray(R.array.recommend_desc)
        val recomPrice = resources.getStringArray(R.array.recommend_price)
        val recomUrl  = resources.getStringArray(R.array.recommend_url)
        val recomLoc = resources.getStringArray(R.array.recommend_location)
        val listDummyRecomData = ArrayList<DummyRecommendData>()
        for (i in recomTitle.indices){
            listDummyRecomData.add(
                DummyRecommendData(
                    recomDesc = recomDesc[i],
                    recomLoc = recomLoc[i],
                    recomPrice = recomPrice[i],
                    recomTitle = recomTitle[i],
                    recomUrl = recomUrl[i]
                )
            )
        }
        return listDummyRecomData
    }
}