package com.example.togutravelapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.togutravelapp.R
import com.example.togutravelapp.adapter.ListTourGuideAdapter
import com.example.togutravelapp.data.DummyTourGuideData
import com.example.togutravelapp.databinding.ActivityListTourGuideBinding

class ListTourGuideActivity : AppCompatActivity() {
    private lateinit var rvTogu: RecyclerView
    private lateinit var binding: ActivityListTourGuideBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListTourGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rvTogu = binding.rvTogu
        rvTogu.setHasFixedSize(true)
        supportActionBar?.hide()

        setToguData()
    }

    private fun setToguData(){
        val dummyListTogu = getDummyToguData()
        rvTogu.layoutManager = GridLayoutManager(this,2)
        rvTogu.adapter = ListTourGuideAdapter(dummyListTogu)
    }

    private fun getDummyToguData():List<DummyTourGuideData>{

        val imageUrlList = resources.getStringArray(R.array.tgUrl)
        val nameList = resources.getStringArray(R.array.tgName)
        val genderList = resources.getStringArray(R.array.tgGender)
        val ratingList = resources.getStringArray(R.array.tgRating)
        val priceList = resources.getStringArray(R.array.tgPrice)
        val listTogu = ArrayList<DummyTourGuideData>()
        for (i in nameList.indices){
        listTogu.add(
            DummyTourGuideData(
                tgUrl = imageUrlList[i],
                tgName = nameList[i],
                tgGender = genderList[i],
                tgRating = ratingList[i],
                tgPrice = priceList[i],
            )
        )
        }
        return listTogu
    }
}