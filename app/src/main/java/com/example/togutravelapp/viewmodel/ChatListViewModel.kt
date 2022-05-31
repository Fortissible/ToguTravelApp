package com.example.togutravelapp.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.togutravelapp.data.DummyTourGuideData
import com.example.togutravelapp.data.MessageData
import com.google.firebase.database.FirebaseDatabase

class ChatListViewModel: ViewModel(){
    private val _chatList = MutableLiveData<List<MessageData>>()
    private val _loadingScreen = MutableLiveData<Boolean>()
    private val _userData = MutableLiveData<MutableList<MessageData>>()
    val chatList: LiveData<List<MessageData>> = _chatList
    val loadingScreen : LiveData<Boolean> = _loadingScreen
    val userData : LiveData<MutableList<MessageData>> = _userData

    fun getChatListFromFbDb(userUid : String, fbDatabase: FirebaseDatabase){
        _loadingScreen.value = true
        fbDatabase.reference.child(userUid).child("users").get().addOnCompleteListener {
            val latestChat = mutableListOf<MessageData>()
            it.result.children.forEach { listUsers ->
                val temporary = mutableListOf<MessageData>()
                val count = listUsers.child("messages").childrenCount.toInt()
                listUsers.child("messages").children.forEach { msgData ->
                    if (count != 0 && userUid != msgData.child("uid").value.toString()) {
                        temporary.add(
                            MessageData(
                                msg = msgData.child("msg").value.toString(),
                                name = msgData.child("name").value.toString(),
                                uid = msgData.child("uid").value.toString(),
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

    fun searchUserFromDbFb(fbDatabase: FirebaseDatabase, username : String?){
        _loadingScreen.value = true
        fbDatabase.reference.child("listUsers").get().addOnCompleteListener { listUid ->
            listUid.result.children.forEach { listIdUsername ->
                listIdUsername.children.forEach { nameData ->
                    if (username == nameData.value.toString())
                        _userData.value!!.add(MessageData(
                            name = nameData.value.toString()
                        ))
                }
            }
            _loadingScreen.value = false
        }.addOnFailureListener {
            _loadingScreen.value = false
        }
    }
}