package com.example.togutravelapp.activity.fragment

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.togutravelapp.adapter.SectionsPagerAdapter
import com.example.togutravelapp.data.ListWisataResponseItem
import com.example.togutravelapp.databinding.FragmentOverviewBinding
import com.google.android.libraries.places.api.Places

class OverviewFragment : Fragment() {
    private var _binding : FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var locationOverviewData = ListWisataResponseItem()
        var geodecodeLoc = ""
        val appInfo: ApplicationInfo = requireContext()
            .applicationContext
            .packageManager.getApplicationInfo(
                requireContext().applicationContext.packageName,
                PackageManager.GET_META_DATA
            )
        val value = appInfo.metaData["com.google.android.geo.API_KEY"]
        val apiKey = value.toString()
        if (!Places.isInitialized()) {
            Places.initialize(requireContext().applicationContext, apiKey)
        }

        val bundle = this.arguments
        if (bundle != null) {
            locationOverviewData = bundle.getParcelable<ListWisataResponseItem>(SectionsPagerAdapter.EXTRA_LOCATION_DATA) as ListWisataResponseItem
            geodecodeLoc = bundle.getString(SectionsPagerAdapter.EXTRA_GEODECODE_LOCATION,"Bandung")
        }
        binding.apply {
            locationDetailDesc.text = locationOverviewData.keterangan.toString()
            locationDetailLocation.text = geodecodeLoc
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}