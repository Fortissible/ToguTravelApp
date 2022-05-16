package com.example.togutravelapp.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.togutravelapp.activity.fragment.ObjectFragment
import com.example.togutravelapp.activity.fragment.OverviewFragment
import com.example.togutravelapp.activity.fragment.RecommendationFragment

class SectionsPagerAdapter(activity: AppCompatActivity):FragmentStateAdapter(activity) {
    private lateinit var uName: String

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var whatfragment: Fragment? = null
        when (position) {

            0 -> {
                whatfragment = OverviewFragment()
                /*
                val mBundle = Bundle()
                mBundle.putString(EXTRA_USERNAME,uName)
                whatfragment.arguments = mBundle*/
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

    fun setUname(username: String){
        uName = username
    }
}