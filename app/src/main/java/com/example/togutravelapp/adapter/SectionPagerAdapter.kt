package com.example.togutravelapp.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.togutravelapp.activity.fragment.ObjectFragment
import com.example.togutravelapp.activity.fragment.OverviewFragment
import com.example.togutravelapp.activity.fragment.RecommendationFragment
import com.example.togutravelapp.data.ListWisataResponseItem

class SectionsPagerAdapter(activity: AppCompatActivity):FragmentStateAdapter(activity) {
    private lateinit var dataLocation: ListWisataResponseItem
    private lateinit var geodecodeResult: String

    companion object {
        const val EXTRA_LOCATION_DATA = "extra_username"
        const val EXTRA_GEODECODE_LOCATION = "extra_geo"
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var whatfragment: Fragment? = null
        when (position) {

            0 -> {
                whatfragment = OverviewFragment()
                val mBundle = Bundle()
                mBundle.putParcelable(EXTRA_LOCATION_DATA,dataLocation)
                mBundle.putString(EXTRA_GEODECODE_LOCATION,geodecodeResult)
                whatfragment.arguments = mBundle
            }
            1 -> {
                whatfragment = ObjectFragment()
            }
            2 -> {
                whatfragment = RecommendationFragment()
            }
        }
        return whatfragment as Fragment
    }

    fun setDataLocation(data: ListWisataResponseItem, geodecode : String){
        dataLocation = data
        geodecodeResult = geodecode
    }
}