package com.example.togutravelapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.togutravelapp.R
import com.example.togutravelapp.activity.fragment.ChatFragment
import com.example.togutravelapp.data.DummyObjectData
import com.example.togutravelapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var buttonToDetail: Button
    private lateinit var buttonToTogu: Button
    private lateinit var buttonToListLoc: Button
    private lateinit var buttonToListChat: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        buttonToDetail = binding.btnDetailObj
        buttonToDetail.setOnClickListener {
            val intentToDetail = Intent(this, QRCodeScannerActivity::class.java)
            intentToDetail.putExtra(DetailObjectActivity.EXTRA_DETAIL_OBJECT,DummyObjectData())
            startActivity(intentToDetail)
        }
        buttonToTogu = binding.btnTogu
        buttonToTogu.setOnClickListener {
            val intentToDetail = Intent(this, ListTourGuideActivity::class.java)
            startActivity(intentToDetail)
        }
        buttonToListLoc = binding.listPlaceButton
        buttonToListLoc.setOnClickListener {
            val intentToListLocation = Intent(this, LocationListActivity::class.java)
            startActivity(intentToListLocation)
        }

        buttonToListChat = binding.listChatUser
        buttonToListChat.setOnClickListener {
            val intentToListChat = Intent(this, ChatListActivity::class.java)
            startActivity(intentToListChat)
        }

        disableAllButton()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_scan_barcode, R.id.navigation_location_list, R.id.navigation_tourguide_list
        ).build()

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun disableAllButton(){
        buttonToDetail.isClickable = false
        buttonToListLoc.isClickable = false
        buttonToTogu.isClickable = false
        buttonToListChat.isClickable = false

        buttonToDetail.visibility = View.INVISIBLE
        buttonToListLoc.visibility = View.INVISIBLE
        buttonToTogu.visibility = View.INVISIBLE
        buttonToListChat.visibility = View.INVISIBLE
        binding.helloWorld.visibility = View.INVISIBLE
    }
}