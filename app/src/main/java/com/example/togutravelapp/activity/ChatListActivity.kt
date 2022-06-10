package com.example.togutravelapp.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.togutravelapp.viewmodel.ViewModelFactory
import com.example.togutravelapp.R
import com.example.togutravelapp.activity.fragment.ChatFragment
import com.example.togutravelapp.activity.fragment.ProfileFragment
import com.example.togutravelapp.adapter.ChatListAdapter
import com.example.togutravelapp.data.DummyTourGuideData
import com.example.togutravelapp.data.MessageData
import com.example.togutravelapp.data.repository.UserRepository
import com.example.togutravelapp.databinding.ActivityChatListBinding
import com.example.togutravelapp.viewmodel.ChatListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

class ChatListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatListBinding
    private lateinit var chatListRv : RecyclerView
    private lateinit var auth : FirebaseAuth
    private lateinit var fbDatabase : FirebaseDatabase
    private lateinit var avatar : CircleImageView
    private lateinit var progressBar : ProgressBar
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private val chatListViewModel : ChatListViewModel by viewModels {
        ViewModelFactory.getInstance(this@ChatListActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = Firebase.auth
        avatar = binding.userChatProfile
        fbDatabase = Firebase.database
        searchView = binding.userChatSearch
        chatListRv = binding.chatListRv
        progressBar = binding.loadingChatList
        chatListRv.setHasFixedSize(true)
        val repo = UserRepository(this)
        val ref = fbDatabase.reference

        val userData : DummyTourGuideData = if (auth.currentUser != null){
            val imageUri = auth.currentUser!!.photoUrl!!
            setUserProfileImage(imageUri)
            DummyTourGuideData(
                tgEmail = auth.currentUser!!.email.toString(),
                tgUrl = auth.currentUser!!.photoUrl.toString(),
                tgName = auth.currentUser!!.displayName.toString()
            )
        } else {
            val imageUri = repo.getUserProfileImage()
            setUserProfileImage(imageUri)
            DummyTourGuideData(
                tgEmail = repo.getUserLoginInfoSession().email,
                tgUrl = repo.getUserProfileImage().toString(),
                tgName = repo.getUserLoginInfoSession().nama
            )
        }

        val invalidEmail: String = if (auth.currentUser != null){
            userData.tgEmail.toString()+"-2"
        } else {
            userData.tgEmail.toString()+"-1"
        }
        val validEmail = invalidEmail.replace(".","dot")

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                chatListViewModel.searchUserFromDbFb(fbDatabase,query,userData.tgName.toString())
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatListViewModel.getChatListFromFbDb(validEmail, fbDatabase)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        chatListViewModel.getChatListFromFbDb(validEmail, fbDatabase)
        chatListViewModel.chatList.observe(this){
            setRecyclerView(it)
        }
        chatListViewModel.loadingScreen.observe(this){
            if (it == true) progressBar.visibility = View.VISIBLE
            else progressBar.visibility = View.INVISIBLE
        }
        chatListViewModel.userData.observe(this){
            if (it.isNotEmpty()) {
                Log.d("OBSERVERNYAH", "$it")
                setRecyclerView(it)
            }
        }

        avatar.setOnClickListener {
            disableAllButton()
            val fragment = ProfileFragment()
            val mBundle = Bundle()
            mBundle.putString(ChatFragment.MESSAGES_TYPE,"chatlist")
            fragment.arguments = mBundle
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.chat_list_activity, fragment, ProfileFragment::class.java.simpleName)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setRecyclerView(data : List<MessageData>){
        chatListRv.layoutManager = LinearLayoutManager(this)
        val adapter = ChatListAdapter(data)
        chatListRv.adapter = adapter

        adapter.setOnItemClickCallback(object: ChatListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: MessageData) {
                val fragment = ChatFragment()
                val mBundle = Bundle()
                mBundle.putString(ChatFragment.MESSAGES_PERSON,data.email)
                mBundle.putString(ChatFragment.MESSAGES_NAME,data.name)
                mBundle.putString(ChatFragment.MESSAGES_URL,data.profileUrl)
                mBundle.putString(ChatFragment.MESSAGES_TYPE,"chatlist")
                fragment.arguments = mBundle
                val fragmentManager = supportFragmentManager.findFragmentByTag(ChatFragment::class.java.simpleName)
                if (fragmentManager !is ChatFragment){
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.chat_list_activity, fragment, ChatFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                }
                disableAllButton()
            }
        })
    }

    private fun disableAllButton(){
        chatListRv.isClickable = false
        chatListRv.visibility = View.INVISIBLE
    }

    fun enableAllButton(){
        chatListRv.isClickable = true
        chatListRv.visibility = View.VISIBLE
    }

    private fun setUserProfileImage(imageUri : Uri){
        Glide.with(this)
            .load(imageUri)
            .centerCrop()
            .into(avatar)
    }
}