package com.example.togutravelapp.activity.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.togutravelapp.R
import com.example.togutravelapp.activity.ChatListActivity
import com.example.togutravelapp.activity.LoginActivity
import com.example.togutravelapp.data.repository.UserRepository
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
    private var type : String? = null

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
        val repo = UserRepository(requireContext())
        auth = Firebase.auth
        val bundle = arguments
        if (bundle != null)
            type = arguments?.getString(ChatFragment.MESSAGES_TYPE)!!

        if (auth.currentUser != null) {
            val name = auth.currentUser!!.displayName.toString()
            val urlProfile = auth.currentUser!!.photoUrl
            val email = auth.currentUser!!.email
            setView(name, urlProfile!! , email!!)
        } else {
            val infoSession = repo.getUserLoginInfoSession()
            val name = infoSession.nama.toString()
            val urlProfile = repo.getUserProfileImage()
            val email = infoSession.email.toString()
            setView(name, urlProfile , email)
        }

        val firebaseUser = auth.currentUser
        if (firebaseUser == null && repo.getUserTokenSession() == "") {
            // Not signed in, launch the Login activity
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            return
        }

        binding.buttonLogout.setOnClickListener {
            signOut(repo)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (type == "chatlist")
            (activity as ChatListActivity).enableAllButton()
        _binding = null
    }

    private fun signOut(repo : UserRepository){
        auth.signOut()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("628388227660-f6smkp93dr5bn1ud9bh0neh58nn9n12h.apps.googleusercontent.com")
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        googleSignInClient.signOut()
        repo.clearInfoSession()
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }

    private fun setView(name : String, photoUrl: Uri, email: String){
        binding.nameProfile.text = name
        binding.emailProfile.text = email
        Glide.with(this)
            .load(photoUrl)
            .placeholder(R.drawable.propict)
            .centerCrop()
            .into(binding.chatAva)
    }
}