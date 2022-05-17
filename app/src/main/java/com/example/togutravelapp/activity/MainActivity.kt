package com.example.togutravelapp.activity

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.togutravelapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var buttonToDetail: Button
    private lateinit var buttonToListLoc: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        buttonToDetail = binding.button
        buttonToListLoc = binding.button2
        buttonToDetail.setOnClickListener {
            val intentToDetail = Intent(this, DetailLocationActivity::class.java)
            startActivity(intentToDetail)
        }
        buttonToListLoc.setOnClickListener {
            val intentToListLocation = Intent(this, LocationListActivity::class.java)
            startActivity(intentToListLocation)
        }
    }
}