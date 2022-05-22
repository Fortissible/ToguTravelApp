package com.example.togutravelapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.togutravelapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var buttonToDetail: Button
    private lateinit var buttonToTogu: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        buttonToDetail = binding.btnDetailObj
        buttonToDetail.setOnClickListener {
            val intentToDetail = Intent(this, DetailObjectActivity::class.java)
            startActivity(intentToDetail)
        }
        buttonToTogu = binding.btnTogu
        buttonToTogu.setOnClickListener {
            val intentToDetail = Intent(this, ListTourGuideActivity::class.java)
            startActivity(intentToDetail)
        }
    }
}