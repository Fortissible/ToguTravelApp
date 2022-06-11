package com.example.togutravelapp.activity.fragment

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.togutravelapp.activity.DetailLocationActivity
import com.example.togutravelapp.adapter.ListRecommendationAdapter
import com.example.togutravelapp.data.ListWisataResponseItem
import com.example.togutravelapp.databinding.FragmentRecommendationBinding
import com.example.togutravelapp.viewmodel.LocationListViewModel
import com.example.togutravelapp.viewmodel.ViewModelFactory
import java.io.IOException
import java.util.*

class RecommendationFragment : Fragment() {
    private var _binding: FragmentRecommendationBinding? = null
    private val binding get() = _binding!!
    private lateinit var recommendarionRv : RecyclerView
    private lateinit var progressBar : ProgressBar
    private val locationListViewModel: LocationListViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRecommendationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recommendarionRv = binding.locationDetailRecomRv
        recommendarionRv.setHasFixedSize(true)

        val listWisata = mutableListOf<ListWisataResponseItem>()
        progressBar = binding.loadingRecommendation
        locationListViewModel.loadingScreen.observe(requireActivity()){
            if (it == true) progressBar.visibility = View.VISIBLE
            else progressBar.visibility = View.INVISIBLE
        }
        locationListViewModel.getlistWisata()
        locationListViewModel.listWisata.observe(requireActivity()){ items ->
            listWisata.clear()
            var counter = 0
            for (item in items){
                if (counter >= 15) break
                listWisata.add(item)
                counter +=1
            }
            setRecommendationData(listWisata)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRecommendationData(listLocation: List<ListWisataResponseItem>){
        recommendarionRv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ListRecommendationAdapter(listLocation)
        recommendarionRv.adapter = adapter
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