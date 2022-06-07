package com.example.togutravelapp.activity.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.togutravelapp.R
import com.example.togutravelapp.activity.DetailLocationActivity
import com.example.togutravelapp.adapter.ListRecommendationAdapter
import com.example.togutravelapp.data.DummyRecommendData
import com.example.togutravelapp.databinding.FragmentLocationListBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LocationListFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentLocationListBinding? = null
    private val binding get() = _binding!!
    private lateinit var locationRv : RecyclerView
    private lateinit var profilePic : ImageView
    private lateinit var mMap: GoogleMap
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profilePic = binding.locationListProfilepic
        auth = Firebase.auth

        Glide.with(this)
            .load(auth.currentUser!!.photoUrl)
            .placeholder(R.drawable.ic_baseline_person_24)
            .centerCrop()
            .into(profilePic)

        locationRv = binding.locationListRv
        locationRv.setHasFixedSize(true)

        profilePic.setOnClickListener {
            val fragment = parentFragmentManager.findFragmentByTag(ProfileFragment::class.java.simpleName)
            if (fragment !is ProfileFragment) {
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, ProfileFragment(), ProfileFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
        }

        setDummyListLocationData()
        setDropDown()

        val mMapsFragment = childFragmentManager.findFragmentById(R.id.map_location) as SupportMapFragment
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

    override fun onResume() {
        super.onResume()
        setDropDown()
    }

    private fun setDummyListLocationData(){
        val dummyListLocation = getDummyListLocationData()
        locationRv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ListRecommendationAdapter(dummyListLocation)
        locationRv.adapter = adapter

        adapter.setOnItemClickCallback(object: ListRecommendationAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DummyRecommendData) {
                Log.d("Clicked Item", "$data")
                val intentToDetail = Intent(activity, DetailLocationActivity::class.java)
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

    private fun setDropDown(){
        val dropdownItemPrice = resources.getStringArray(R.array.sort_by_price)
        val dropdownItemRange = resources.getStringArray(R.array.sort_by_range)
        val arrayAdapterPrice = ArrayAdapter(requireContext(),R.layout.dropdown_item,dropdownItemPrice)
        binding.buttonDropdownSortPrice.setAdapter(arrayAdapterPrice)
        val arrayAdapterRange = ArrayAdapter(requireContext(),R.layout.dropdown_item,dropdownItemRange)
        binding.buttonDropdownSortRange.setAdapter(arrayAdapterRange)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}