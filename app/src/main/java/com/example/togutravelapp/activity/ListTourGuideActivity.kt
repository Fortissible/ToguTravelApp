package com.example.togutravelapp.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.togutravelapp.R
import com.example.togutravelapp.activity.fragment.ChatFragment
import com.example.togutravelapp.adapter.ListTourGuideAdapter
import com.example.togutravelapp.data.DummyTourGuideData
import com.example.togutravelapp.databinding.ActivityListTourGuideBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

class ListTourGuideActivity : AppCompatActivity() {
    private lateinit var rvTogu: RecyclerView
    private lateinit var binding: ActivityListTourGuideBinding
    private lateinit var profile : CircleImageView
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListTourGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profile = binding.listToguProfile
        auth = Firebase.auth

        Glide.with(this)
            .load(auth.currentUser!!.photoUrl)
            .centerCrop()
            .into(profile)
            
        rvTogu = binding.rvTogu
        rvTogu.setHasFixedSize(true)
        supportActionBar?.hide()

        setToguData()
    }

    private fun setToguData(){
        val dummyListTogu = getDummyToguData()
        val adapter = ListTourGuideAdapter(dummyListTogu)
        rvTogu.layoutManager = GridLayoutManager(this,2)
        rvTogu.adapter = adapter
        adapter.setOnItemClickCallback(object : ListTourGuideAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DummyTourGuideData) {
                val fragment = ChatFragment()
                val mBundle = Bundle()
                mBundle.putString(ChatFragment.MESSAGES_PERSON,"4mLpFIu1pUf07BubkaVg1czWg6F3")
                mBundle.putString(ChatFragment.MESSAGES_NAME,"Wildan Fajri Alfarabi A2001F0016")
                mBundle.putString(ChatFragment.MESSAGES_URL,"https://lh3.googleusercontent.com/a-/AOh14GiCsrcPihgrO7BbYMNYC2YSNcqeGufLywA8FL6v=s96-c")
                mBundle.putString(ChatFragment.MESSAGES_TYPE,"tourguide")
                fragment.arguments = mBundle
                val fragmentManager = supportFragmentManager.findFragmentByTag(ChatFragment::class.java.simpleName)
                if (fragmentManager !is ChatFragment){
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.list_tour_guide_activity, fragment, ChatFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                }
            }
        })
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