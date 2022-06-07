package com.example.togutravelapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.togutravelapp.R
import com.example.togutravelapp.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val btnCancel = binding.CancelButton
        btnCancel.setOnClickListener {
            val intent = Intent(this, LoginActivity ::class.java)
            startActivity(intent)
            finish()
        }
    }
}