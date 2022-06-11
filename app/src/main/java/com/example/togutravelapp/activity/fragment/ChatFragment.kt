package com.example.togutravelapp.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.togutravelapp.R
import com.example.togutravelapp.activity.ChatListActivity
import com.example.togutravelapp.adapter.FbMessageAdapter
import com.example.togutravelapp.data.DummyTourGuideData
import com.example.togutravelapp.data.MessageData
import com.example.togutravelapp.data.repository.UserRepository
import com.example.togutravelapp.databinding.FragmentChatBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatdb : FirebaseDatabase
    private lateinit var chatAdapter : FbMessageAdapter
    private lateinit var personAvatar : CircleImageView
    private lateinit var personName : TextView
    private lateinit var messagesRv : RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var type: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager = LinearLayoutManager(requireActivity())
        manager.stackFromEnd = true
        messagesRv = binding.chatRv
        auth = Firebase.auth
        val repo = UserRepository(requireContext())

        chatdb = Firebase.database
        personAvatar = binding.chatAva
        personName = binding.chatName

        // DATA TOUR GUIDE
        val bundle = arguments
        val emailPerson = bundle!!.getString(MESSAGES_PERSON)!!
        val namePerson = bundle.getString(MESSAGES_NAME)!!
        val urlImagePerson = bundle.getString(MESSAGES_URL)!!
        type = bundle.getString(MESSAGES_TYPE)!!

        // DATA USER
        val validEmailUser: String
        val userData : DummyTourGuideData = if (auth.currentUser != null){
            validEmailUser = (auth.currentUser!!.email.toString()+"-2").replace(".","dot")
            DummyTourGuideData(
                tgEmail = validEmailUser,
                tgUrl = auth.currentUser!!.photoUrl.toString(),
                tgName = auth.currentUser!!.displayName.toString()
            )
        } else {
            validEmailUser = (repo.getUserLoginInfoSession().email.toString()+"-1").replace(".","dot")
            DummyTourGuideData(
                tgEmail = validEmailUser,
                tgUrl = repo.getUserProfileImage().toString(),
                tgName = repo.getUserLoginInfoSession().nama
            )
        }

        //val invalidEmailPerson = usersLogin.tgEmail.toString()+"-1"
        //val validEmailPerson = invalidEmail.replace(".","dot")

        // msg Reference User -> input msg ke db si user
        val msgRefUser = chatdb.reference.child(userData.tgEmail.toString())
            .child(MESSAGES_PERSON)
            .child(emailPerson)
            .child(MESSAGES_CHILD)

        // msg Reference TG -> input ke db si TG
        val msgRefPerson = chatdb.reference.child(emailPerson)
            .child(MESSAGES_PERSON)
            .child(userData.tgEmail.toString())
            .child(MESSAGES_CHILD)

        val options = FirebaseRecyclerOptions.Builder<MessageData>()
            .setQuery(msgRefUser, MessageData::class.java)
            .build()

        messagesRv.layoutManager = manager
        chatAdapter = FbMessageAdapter(options,userData.tgName)
        messagesRv.adapter = chatAdapter

        chatAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                val msgCount = chatAdapter.itemCount
                val lastPos = manager.findLastCompletelyVisibleItemPosition()
                if (lastPos == -1 ||
                    (positionStart >= (msgCount - 1) && lastPos == (positionStart - 1))){
                    messagesRv.scrollToPosition(positionStart)
                }
            }
        })

        personName.text = namePerson
        Glide.with(this)
            .load(urlImagePerson)
            .placeholder(R.drawable.propict)
            .centerCrop()
            .into(personAvatar)

        binding.sendChatButton.setOnClickListener {
            val msg = MessageData(
                binding.chatMessageEdittext.text.toString(),
                userData.tgName.toString(),
                userData.tgEmail.toString(),
                userData.tgUrl.toString(),
                Date().time
            )
            if (binding.chatMessageEdittext.text.toString().isNotBlank()) {
                msgRefUser.push().setValue(msg) { e,_ ->
                    if (e != null) Toast.makeText(requireContext(), "error sending message" + e.message, Toast.LENGTH_SHORT).show()
                }
                msgRefPerson.push().setValue(msg)
            }
            binding.chatMessageEdittext.setText("")
        }
    }

    override fun onResume() {
        super.onResume()
        chatAdapter.startListening()
    }

    override fun onPause() {
        chatAdapter.stopListening()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (type == "chatlist")
            (activity as ChatListActivity).enableAllButton()
        else{
            (parentFragment as ListTourGuideFragment).isMessageButtonActive(true)
        }
    }

    companion object {
        const val MESSAGES_CHILD = "messages"
        const val MESSAGES_PERSON = "users"
        const val MESSAGES_NAME = "name"
        const val MESSAGES_URL = "url"
        const val MESSAGES_TYPE = "type"
    }
}