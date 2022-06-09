package com.example.togutravelapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.togutravelapp.data.MessageData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ChatListViewModel: ViewModel(){
    private val _chatList = MutableLiveData<List<MessageData>>()
    private val _loadingScreen = MutableLiveData<Boolean>()
    private val _userData = MutableLiveData<MutableList<MessageData>>()
    val chatList: LiveData<List<MessageData>> = _chatList
    val loadingScreen : LiveData<Boolean> = _loadingScreen
    val userData : LiveData<MutableList<MessageData>> = _userData

    fun getChatListFromFbDb(email : String, fbDatabase: FirebaseDatabase){
        _loadingScreen.value = true
        fbDatabase.reference.child(email).child("users").get().addOnCompleteListener {
            val latestChat = mutableListOf<MessageData>()
            it.result.children.forEach { listUsers ->
                val temporary = mutableListOf<MessageData>()
                val count = listUsers.child("messages").childrenCount.toInt()
                listUsers.child("messages").children.forEach { msgData ->
                    if (count != 0 && email != msgData.child("email").value.toString()) {
                        temporary.add(
                            MessageData(
                                msg = msgData.child("msg").value.toString(),
                                name = msgData.child("name").value.toString(),
                                email = msgData.child("email").value.toString(),
                                profileUrl = msgData.child("profileUrl").value.toString(),
                                timestamp = msgData.child("timestamp").value as Long
                            )
                        )
                    }
                }
                for (i in temporary.indices){
                    if (i == temporary.size-1){
                        latestChat.add(temporary[i])
                    }
                }
            }
            _chatList.value = latestChat
            _loadingScreen.value = false
        }.addOnFailureListener {
            _loadingScreen.value = false
        }
    }

    fun searchUserFromDbFb(fbDatabase: FirebaseDatabase, username : String?, currentUser: String){
        _loadingScreen.value = true
        fbDatabase.reference.child("listUsers").get().addOnCompleteListener { listEmail ->
            val matchingUser = mutableListOf<MessageData>()
            listEmail.result.children.forEach { listIdUsername ->
                val temporary = mutableListOf<MessageData>()
                listIdUsername.children.forEach { data ->
                    val name = data.child("tgName").value.toString()
                    val url = data.child("tgUrl").value.toString()
                    val email = data.child("tgEmail").value.toString()
                    Log.d("CEK ISI SEARCHNYA", name)
                    if (name.lowercase().contains(username?.lowercase()?:"notfounds") && name != currentUser)
                        temporary.add(MessageData(
                            name = name,
                            email = email,
                            profileUrl = url
                            )
                        )
                }
                for (i in temporary.indices){
                    if (i == temporary.size-1){
                        matchingUser.add(temporary[i])
                    }
                }
            }
            _userData.value = matchingUser
            _loadingScreen.value = false
        }.addOnFailureListener {
            _loadingScreen.value = false
        }
    }
}