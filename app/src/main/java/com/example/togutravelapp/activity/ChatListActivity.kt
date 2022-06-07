package com.example.togutravelapp.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidintermediate_sub1_wildanfajrialfarabi.ui.ViewModelFactory
import com.example.togutravelapp.R
import com.example.togutravelapp.activity.fragment.ChatFragment
import com.example.togutravelapp.activity.fragment.ProfileFragment
import com.example.togutravelapp.adapter.ChatListAdapter
import com.example.togutravelapp.data.MessageData
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
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = Firebase.auth
        fbDatabase = Firebase.database
        avatar = binding.userChatProfile
        searchView = binding.userChatSearch
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                chatListViewModel.searchUserFromDbFb(fbDatabase,query,auth)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        val ref = fbDatabase.reference
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatListViewModel.getChatListFromFbDb(auth.currentUser!!.uid, fbDatabase)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        chatListRv = binding.chatListRv
        chatListRv.setHasFixedSize(true)

        progressBar = binding.loadingChatList

        Glide.with(this)
            .load(auth.currentUser!!.photoUrl)
            .centerCrop()
            .into(avatar)

        chatListViewModel.getChatListFromFbDb(auth.currentUser!!.uid, fbDatabase)
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
            val fragment = supportFragmentManager.findFragmentByTag(ProfileFragment::class.java.simpleName)
            if (fragment !is ProfileFragment) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.chat_list_activity, ProfileFragment(), ProfileFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
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
                mBundle.putString(ChatFragment.MESSAGES_PERSON,data.uid)
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
}