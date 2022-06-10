package com.example.togutravelapp.activity.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.togutravelapp.R
import com.example.togutravelapp.activity.DetailLocationActivity
import com.example.togutravelapp.adapter.ListRecommendationAdapter
import com.example.togutravelapp.data.DummyRecommendData
import com.example.togutravelapp.data.ListWisataResponseItem
import com.example.togutravelapp.databinding.FragmentRecommendationBinding
import com.example.togutravelapp.viewmodel.LocationListViewModel

class RecommendationFragment : Fragment() {
    private var _binding: FragmentRecommendationBinding? = null
    private val binding get() = _binding!!
    private lateinit var recommendarionRv : RecyclerView

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
        setRecommendationData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRecommendationData(){
        val ListRecommendationData = getRecommendationData()
        recommendarionRv.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = ListRecommendationAdapter(ListRecommendationData)
        recommendarionRv.adapter = adapter
        adapter.setOnItemClickCallback(object : ListRecommendationAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ListWisataResponseItem) {
                val intentToLocation = Intent(requireActivity(),DetailLocationActivity::class.java)
                intentToLocation.putExtra(DetailLocationActivity.EXTRA_LOCATIONDETAIL,data)
                startActivity(intentToLocation)
            }
        })

    }

    private fun getRecommendationData(): List<ListWisataResponseItem> {
        val listWisata = mutableListOf<ListWisataResponseItem>()
        val listLocation = ViewModelProvider(this,
            ViewModelProvider.NewInstanceFactory())[LocationListViewModel::class.java]
        listLocation.getlistWisata()
        listLocation.listWisata.observe(requireActivity()) { item ->
            item.forEach {
                val listWisataTemp = ListWisataResponseItem(
                    nama= it.nama,
                    keterangan = it.keterangan,
                    harga = it.harga,
                    urlImage = it.urlImage,
                    lokasi = it.lokasi,
                    id = it.id,
                    jenis = it.jenis,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    rating = it.rating,
                )
                listWisata.add(listWisataTemp)
            }
        }
        return listWisata
    }
}