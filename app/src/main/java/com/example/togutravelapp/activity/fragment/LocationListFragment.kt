package com.example.togutravelapp.activity.fragment

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView

import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.togutravelapp.R
import com.example.togutravelapp.activity.DetailLocationActivity
import com.example.togutravelapp.activity.LoginActivity
import com.example.togutravelapp.adapter.ListRecommendationAdapter

import com.example.togutravelapp.data.ListWisataResponseItem
import com.example.togutravelapp.data.repository.UserRepository
import com.example.togutravelapp.databinding.FragmentLocationListBinding
import com.example.togutravelapp.viewmodel.LocationListViewModel
import com.example.togutravelapp.viewmodel.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.IOException
import java.util.*

class LocationListFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentLocationListBinding? = null
    private val binding get() = _binding!!
    private lateinit var locationRv : RecyclerView
    private lateinit var profilePic : ImageView
    private lateinit var locationSearch : SearchView
    private lateinit var mMap: GoogleMap
    private lateinit var auth : FirebaseAuth
    private val locationListViewModel : LocationListViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = requireActivity().intent.getIntExtra(LoginActivity.TYPE,1) //1 -> api ; 2 -> google
        profilePic = binding.locationListProfilepic

        val repo = UserRepository(requireContext())
        val imageUri = repo.getUserProfileImage()

        if (type == 1) {
            Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.propict)
                .centerCrop()
                .into(profilePic)

        } else {
            auth = Firebase.auth

            Glide.with(this)
                .load(auth.currentUser!!.photoUrl)
                .placeholder(R.drawable.propict)
                .centerCrop()
                .into(profilePic)
        }
        locationSearch = binding.locationSearch
        locationSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                locationListViewModel.getlistWisata(query)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        locationRv = binding.locationListRv
        locationRv.setHasFixedSize(true)

        profilePic.setOnClickListener {
            val fragment = parentFragmentManager
                .findFragmentByTag(ProfileFragment::class.java.simpleName)
            if (fragment !is ProfileFragment) {
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment,
                        ProfileFragment(),
                        ProfileFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
        }

        val listWisata = mutableListOf<ListWisataResponseItem>()

        locationListViewModel.getlistWisata()
        locationListViewModel.listWisata.observe(requireActivity()){ items ->
            listWisata.clear()
            var counter = 0
            for (item in items){
                if (counter >= 15) break
                listWisata.add(item)
                counter +=1
            }
            setLocationListData(listWisata)
            setLocationListMaps(listWisata)
        }

        setDropDown()

        val mMapsFragment = childFragmentManager
            .findFragmentById(R.id.map_location) as SupportMapFragment
        mMapsFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val bandung = LatLng(-6.920541, 107.6272745 )
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bandung,8.5f))

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }

    override fun onResume() {
        super.onResume()
        setDropDown()
    }

    private fun setLocationListData(listLocation : List<ListWisataResponseItem>){
        locationRv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ListRecommendationAdapter(listLocation)
        locationRv.adapter = adapter
        adapter.setOnItemClickCallback(object: ListRecommendationAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ListWisataResponseItem) {
                Log.d("Clicked Item", "$data")
                val geodecodeLocation = reverseGeocoder(
                    data.latitude!!.toDouble(),
                    data.longitude!!.toDouble()
                )
                val intentToDetail = Intent(activity, DetailLocationActivity::class.java)
                intentToDetail.putExtra(
                    DetailLocationActivity.EXTRA_LOCATIONDETAIL,
                    data
                )
                intentToDetail.putExtra(
                    DetailLocationActivity.EXTRA_GEODECODE_LOC,
                    geodecodeLocation
                )
                startActivity(intentToDetail)
            }
        })
    }

    private fun setLocationListMaps(listLocation : List<ListWisataResponseItem>){
        mMap.clear()
        for (location in listLocation){
            if (!location.latitude.isNullOrEmpty() && !location.longitude.isNullOrEmpty()){
                mMap.addMarker(MarkerOptions()
                    .position(LatLng(location.latitude.toDouble(),location.longitude.toDouble()))
                    .title(location.nama)
                    .snippet(location.jenis)
                )
            }
        }
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
        _binding = null
        super.onDestroyView()
    }

    private fun reverseGeocoder(lat : Double?, lon : Double?) : String {
        val revGeocoder = Geocoder(context, Locale.getDefault())
        if (lat != null && lon != null) {
            try {
                val addressGeo: List<Address> = revGeocoder.getFromLocation(lat, lon, 1)
                val addressDecoded = buildString {
                    if (addressGeo.isNotEmpty()) {
                        append(addressGeo[0].subAdminArea).append("\n")
                        append(addressGeo[0].locality).append(", ")
                        append(addressGeo[0].adminArea).append(", ")
                        append(addressGeo[0].countryName).append(", ")
                        append(addressGeo[0].postalCode)
                    }
                    else
                        append(failToDecodeCoordinates())
                }
                return addressDecoded
            } catch (e: IOException) {
                Toast.makeText(context,"Unable connect to Geocoder", Toast.LENGTH_SHORT).show()
                return failToDecodeCoordinates()
            }
        }
        else {
            return failToDecodeCoordinates()
        }
    }

    private fun failToDecodeCoordinates():String{
        Toast.makeText(context,"Geodecoder failed to decode", Toast.LENGTH_SHORT).show()
        val stringBuilder = StringBuilder().append("Bandung")
        return stringBuilder.toString()
    }
}