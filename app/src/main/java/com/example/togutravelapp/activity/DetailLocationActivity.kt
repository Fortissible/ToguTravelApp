package com.example.togutravelapp.activity

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.togutravelapp.R
import com.example.togutravelapp.adapter.ListImageAdapter
import com.example.togutravelapp.adapter.SectionsPagerAdapter
import com.example.togutravelapp.data.DummyURL
import com.example.togutravelapp.data.ListWisataResponseItem
import com.example.togutravelapp.databinding.ActivityDetailLocationBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.text.DecimalFormat

class DetailLocationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailLocationBinding
    private lateinit var imageRv : RecyclerView
    private lateinit var locationProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val locationDetail = intent.getParcelableExtra<ListWisataResponseItem>(EXTRA_LOCATIONDETAIL) as ListWisataResponseItem
        val locationGeodecode = intent.getStringExtra(EXTRA_GEODECODE_LOC)
        val format = DecimalFormat("#,###")
        val priceFormated = StringBuilder().append("Rp.").append(format.format(locationDetail.harga))


        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.setDataLocation(locationDetail,locationGeodecode!!)
        val viewPager: ViewPager2 = binding.locationDetailViewpager2
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.locationDetailTabs
        TabLayoutMediator(tabs, viewPager){ tab,pos ->
            tab.text = TAB_TITLES[pos]
        }.attach()

        locationProfile = binding.locationDetailImage
        Glide.with(this)
            .load(locationDetail.urlImage)

            .placeholder(R.drawable.ios_android_free)
            .centerCrop()
            .into(locationProfile)

        binding.apply {
            locationDetailName.text = locationDetail.nama ?: "Not Found"
            locationDetailAddr.text = locationDetail.lokasi ?: "Bandung"
            locationDetailPrice.text = priceFormated.toString()
            locationDetailCategory.text = locationDetail.jenis
        }

        imageRv = binding.locationDetailListImageRv
        imageRv.setHasFixedSize(true)
        setImageData()
    }

    private fun setImageData(){
        val listDummyData = setDummyImageURL()
        imageRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = ListImageAdapter(listDummyData)
        imageRv.adapter = adapter
        /*
        adapter.setOnItemClickCallback(object: ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ItemsItem) {
                val detailed = data.login
                val intentToDetail = Intent(this@MainActivity, UserDetailActivity::class.java)
                intentToDetail.putExtra(EXTRA_USERNAME, detailed)
                startActivity(intentToDetail)
            }
        })*/
    }

    private fun setDummyImageURL() : List<DummyURL>{
        val listURL = arrayOf("https://www.wisataidn.com/wp-content/uploads/2020/07/Lokasi-Museum-Geologi-Bandung-750x450.jpg","https://asset.kompas.com/crops/xvwpNYGNMTf2ElXn75uyz22fBQk=/0x0:0x0/750x500/data/photo/2014/08/18/1824308MuseumGeologi031408360918-previewp.jpg","https://cdn.nativeindonesia.com/foto/museum-geologi-bandung/Koleksi-Mineral-Di-Museum-Geologi-Bandung.jpg")
        val listURLDummy = mutableListOf<DummyURL>()
        for (i in listURL){
            listURLDummy.add(
                DummyURL(urlImage = i)
            )
        }
        return listURLDummy
    }

    companion object {
        private val TAB_TITLES = listOf("Tinjauan","Objek Wisata","Rekomendasi")
        const val EXTRA_LOCATIONDETAIL = "location_detail"
        const val EXTRA_GEODECODE_LOC = "location_geodecode"
    }
}