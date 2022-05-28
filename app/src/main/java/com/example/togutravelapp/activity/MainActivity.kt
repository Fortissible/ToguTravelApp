package com.example.togutravelapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.togutravelapp.R
import com.example.togutravelapp.activity.fragment.ChatFragment
import com.example.togutravelapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var buttonToListLoc: Button
    private lateinit var buttonToChat: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        buttonToListLoc = binding.button
        buttonToChat = binding.chatButton
        buttonToListLoc.setOnClickListener {
            val intentToListLocation = Intent(this, LocationListActivity::class.java)
            startActivity(intentToListLocation)
        }
        buttonToChat.setOnClickListener {
            val fragmentManager = supportFragmentManager.findFragmentByTag(ChatFragment::class.java.simpleName)
            if (fragmentManager !is ChatFragment){
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.main_activity,ChatFragment(),ChatFragment::class.java.simpleName)
                    .commit()
            }
        }
    }
}