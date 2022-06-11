package com.example.togutravelapp.activity.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.togutravelapp.R
import com.example.togutravelapp.activity.ChatListActivity
import com.example.togutravelapp.adapter.ListTourGuideAdapter
import com.example.togutravelapp.data.TourguideItem
import com.example.togutravelapp.data.repository.UserRepository
import com.example.togutravelapp.databinding.FragmentListTourGuideBinding
import com.example.togutravelapp.viewmodel.TourGuidesViewModel
import com.example.togutravelapp.viewmodel.ViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

class ListTourGuideFragment : Fragment() {
    private var _binding: FragmentListTourGuideBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvTogu: RecyclerView
    private lateinit var profile : CircleImageView
    private lateinit var searchBar : SearchView
    private lateinit var auth : FirebaseAuth
    private lateinit var progressBar : ProgressBar
    private lateinit var msgButton : FloatingActionButton
    private val tourGuidesViewModel : TourGuidesViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListTourGuideBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profile = binding.listToguProfile
        auth = Firebase.auth
        val repo = UserRepository(requireContext())
        if (auth.currentUser != null){
            val imageUri = auth.currentUser!!.photoUrl.toString()
            setUserProfileImage(imageUri)
        } else {
            val imageUri = repo.getUserProfileImage().toString()
            setUserProfileImage(imageUri)
        }

        progressBar = binding.loadingListTogu
        tourGuidesViewModel.loadingScreen.observe(requireActivity()){
            if (it == true) progressBar.visibility = View.VISIBLE
            else progressBar.visibility = View.INVISIBLE
        }

        msgButton = binding.chatListButton
        msgButton.setOnClickListener {
            intentToMessageActivity()
        }
        rvTogu = binding.rvTogu
        rvTogu.setHasFixedSize(true)

        tourGuidesViewModel.findTourGuides()
        tourGuidesViewModel.tourGuides.observe(requireActivity()){
            if (!it.isNullOrEmpty()) {
                setToguData(it)
            }
        }

        searchBar = binding.searchView
        searchBar.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                tourGuidesViewModel.findTourGuides(query)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        profile.setOnClickListener {
            val fragment = parentFragmentManager.findFragmentByTag(ProfileFragment::class.java.simpleName)
            if (fragment !is ProfileFragment) {
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, ProfileFragment(), ProfileFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    @Suppress("DEPRECATION")
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setToguData(data : List<TourguideItem>){
        val adapter = ListTourGuideAdapter(data)
        rvTogu.layoutManager = GridLayoutManager(activity,2)
        rvTogu.adapter = adapter
        adapter.setOnItemClickCallback(object : ListTourGuideAdapter.OnItemClickCallback{
            override fun onItemClicked(data: TourguideItem) {
                isMessageButtonActive(false)
                val validEmail = (data.email.toString()+"-1").replace(".","dot")
                val fragment = ChatFragment()
                val mBundle = Bundle()
                mBundle.putString(ChatFragment.MESSAGES_PERSON, validEmail)
                mBundle.putString(ChatFragment.MESSAGES_NAME, data.nama)
                mBundle.putString(ChatFragment.MESSAGES_URL, data.urlImage.toString())
                mBundle.putString(ChatFragment.MESSAGES_TYPE,"tourguide")
                fragment.arguments = mBundle
                val fragmentManager = childFragmentManager.findFragmentByTag(ChatFragment::class.java.simpleName)
                if (fragmentManager !is ChatFragment){
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.list_tour_guide_fragment, fragment, ChatFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                }
            }
        })
    }

    private fun intentToMessageActivity(){
        val intent = Intent(activity,ChatListActivity::class.java)
        startActivity(intent)
    }

    private fun setUserProfileImage(url : String){
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.propict)
            .centerCrop()
            .into(profile)
    }

    fun isMessageButtonActive(value : Boolean){
        if (value)
            msgButton.apply {
                visibility = View.VISIBLE
                isClickable = value
            }
        else msgButton.apply {
            visibility = View.GONE
            isClickable = value
        }
    }
}