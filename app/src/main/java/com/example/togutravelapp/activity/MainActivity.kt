package com.example.togutravelapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.togutravelapp.R
import com.example.togutravelapp.activity.fragment.ChatFragment
import com.example.togutravelapp.data.DummyObjectData
import com.example.togutravelapp.databinding.ActivityMainBinding
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
            val intentToDetail = Intent(this, DetailObjectActivity::class.java)
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
    }

    private fun disableAllButton(){
        buttonToDetail.isClickable = false
        buttonToListLoc.isClickable = false
        buttonToTogu.isClickable = false

        buttonToDetail.visibility = View.INVISIBLE
        buttonToListLoc.visibility = View.INVISIBLE
        buttonToTogu.visibility = View.INVISIBLE
    }
}