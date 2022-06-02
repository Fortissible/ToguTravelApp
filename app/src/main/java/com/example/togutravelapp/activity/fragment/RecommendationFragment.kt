package com.example.togutravelapp.activity.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.togutravelapp.R
import com.example.togutravelapp.activity.DetailLocationActivity
import com.example.togutravelapp.adapter.ListRecommendationAdapter
import com.example.togutravelapp.data.DummyRecommendData
import com.example.togutravelapp.databinding.FragmentRecommendationBinding

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
        val dummyListRecommendationData = getDummyRecommendationData()
        recommendarionRv.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = ListRecommendationAdapter(dummyListRecommendationData)
        recommendarionRv.adapter = adapter
        adapter.setOnItemClickCallback(object : ListRecommendationAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DummyRecommendData) {
                val intentToLocation = Intent(requireActivity(),DetailLocationActivity::class.java)
                intentToLocation.putExtra(DetailLocationActivity.EXTRA_LOCATIONDETAIL,data)
                startActivity(intentToLocation)
            }
        })

    }

    private fun getDummyRecommendationData(): List<DummyRecommendData>{
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