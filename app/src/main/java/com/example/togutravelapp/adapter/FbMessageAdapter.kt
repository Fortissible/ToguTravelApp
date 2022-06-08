package com.example.togutravelapp.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.togutravelapp.R
import com.example.togutravelapp.data.MessageData
import com.example.togutravelapp.databinding.ChatItemPeopleBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class FbMessageAdapter( options : FirebaseRecyclerOptions<MessageData>,
    private val currentUser: String?): FirebaseRecyclerAdapter<MessageData,FbMessageAdapter.MessageViewHolder>(options) {

    inner class MessageViewHolder(private val binding: ChatItemPeopleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MessageData) {
            binding.chatContextPeople.text = item.msg
            Glide.with(itemView.context)
                .load(item.profileUrl)
                .circleCrop()
                .into(binding.chatAvatarPeople)
            if (item.timestamp != null) {
                binding.chatTimestampPeople.text = DateUtils.getRelativeTimeSpanString(item.timestamp)
            }
            setTextColor(item.name, binding)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.chat_item_people, parent, false)
        val binding = ChatItemPeopleBinding.bind(view)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: MessageData) {
        holder.bind(model)
    }

    private fun setTextColor(userName: String?, binding: ChatItemPeopleBinding) {
        val set = ConstraintSet()
        if (currentUser == userName && userName != null) {
            set.clone(binding.chatItemConstraintLayout)

            set.setMargin(binding.chatAvatarPeople.id,ConstraintSet.START,0)
            set.setMargin(binding.chatAvatarPeople.id,ConstraintSet.END,24)
            set.clear(binding.chatAvatarPeople.id,ConstraintSet.START)
            set.connect(binding.chatAvatarPeople.id,ConstraintSet.END,
                binding.chatItemConstraintLayout.id, ConstraintSet.END)

            set.setMargin(binding.chatContextPeople.id,ConstraintSet.START,0)
            set.setMargin(binding.chatContextPeople.id,ConstraintSet.END,8)
            set.clear(binding.chatContextPeople.id,ConstraintSet.START)
            set.connect(binding.chatContextPeople.id,ConstraintSet.END,
                binding.chatAvatarPeople.id, ConstraintSet.START)

            set.clear(binding.chatTimestampPeople.id,ConstraintSet.START)
            set.connect(binding.chatTimestampPeople.id,ConstraintSet.END,
                binding.chatContextPeople.id, ConstraintSet.END)

            set.applyTo(binding.chatItemConstraintLayout)

            binding.chatContextPeople.setBackgroundResource(R.drawable.chat_context_background_self)
        } else {
            set.clone(binding.chatItemConstraintLayout)

            set.setMargin(binding.chatAvatarPeople.id,ConstraintSet.START,24)
            set.setMargin(binding.chatAvatarPeople.id,ConstraintSet.END,0)
            set.clear(binding.chatAvatarPeople.id,ConstraintSet.END)
            set.connect(binding.chatAvatarPeople.id,ConstraintSet.START,
                binding.chatItemConstraintLayout.id, ConstraintSet.START)

            set.setMargin(binding.chatContextPeople.id,ConstraintSet.START,8)
            set.setMargin(binding.chatContextPeople.id,ConstraintSet.END,0)
            set.clear(binding.chatContextPeople.id,ConstraintSet.END)
            set.connect(binding.chatContextPeople.id,ConstraintSet.START,
                binding.chatAvatarPeople.id, ConstraintSet.END)

            set.clear(binding.chatTimestampPeople.id,ConstraintSet.END)
            set.connect(binding.chatTimestampPeople.id,ConstraintSet.START,
                binding.chatContextPeople.id, ConstraintSet.START)

            set.applyTo(binding.chatItemConstraintLayout)

            binding.chatContextPeople.setBackgroundResource(R.drawable.chat_context_background_person)
        }
    }
}