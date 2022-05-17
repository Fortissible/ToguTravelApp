package com.example.togutravelapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.togutravelapp.R
import com.example.togutravelapp.adapter.ListRecommendationAdapter
import com.example.togutravelapp.data.DummyRecommendData
import com.example.togutravelapp.databinding.ActivityLocationListBinding

class LocationListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLocationListBinding
    private lateinit var locationRv : RecyclerView
    private lateinit var profilePic : ImageView
    private lateinit var mapsContainer : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapsContainer = binding.locationMapsContainer
        profilePic = binding.locationListProfilepic
        locationRv = binding.locationListRv
        locationRv.setHasFixedSize(true)
        supportActionBar?.hide()

        Glide.with(this)
            .load("https://i0.wp.com/www.cssscript.com/wp-content/uploads/2018/03/Simple-Location-Picker.png?fit=561%2C421&ssl=1")
            .centerCrop()
            .into(mapsContainer)

        setDummyListLocationData()
    }

    private fun setDummyListLocationData(){
        val dummyListLocation = getDummyListLocationData()
        locationRv.layoutManager = LinearLayoutManager(this)
        locationRv.adapter = ListRecommendationAdapter(dummyListLocation)
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