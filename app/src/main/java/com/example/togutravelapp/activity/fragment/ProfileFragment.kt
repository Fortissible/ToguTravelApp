package com.example.togutravelapp.activity.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.togutravelapp.R
import com.example.togutravelapp.activity.LoginActivity
import com.example.togutravelapp.databinding.FragmentLogoutBinding
import com.example.togutravelapp.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        val name = auth.currentUser!!.displayName.toString()
        val urlProfile = auth.currentUser!!.photoUrl
        val email = auth.currentUser!!.email

        binding.nameProfile.text = name
        binding.emailProfile.text = email

        Glide.with(this)
            .load(urlProfile)
            .placeholder(R.drawable.ic_baseline_person_24)
            .centerCrop()
            .into(binding.chatAva)

        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            // Not signed in, launch the Login activity
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            return
        }

        binding.buttonLogout.setOnClickListener {
            signOut()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun signOut(){
        auth.signOut()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("628388227660-f6smkp93dr5bn1ud9bh0neh58nn9n12h.apps.googleusercontent.com")
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        googleSignInClient.signOut()
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }
}