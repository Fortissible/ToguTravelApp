package com.example.togutravelapp.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.togutravelapp.R
import com.example.togutravelapp.data.MessageData
import de.hdodenhof.circleimageview.CircleImageView

class ChatListAdapter(private val data : List<MessageData> ): RecyclerView.Adapter<ChatListAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback{
        fun onItemClicked(data: MessageData)
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val userPhoto : CircleImageView = itemView.findViewById(R.id.chat_person_photo)
        val userName : TextView = itemView.findViewById(R.id.chat_person_name)
        val userLatestChat : TextView = itemView.findViewById(R.id.chat_person_latest_chat)
        val userTimestamp : TextView = itemView.findViewById(R.id.chat_person_timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val item : View = LayoutInflater.from(parent.context).inflate(R.layout.chat_person_item,parent,false)
        return ListViewHolder(item)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val userImg = holder.userPhoto
        val userName = holder.userName
        val userLatestChat = holder.userLatestChat
        val userTimestamp = holder.userTimestamp

        userName.text = data[position].name ?: "NO NAME"
        userLatestChat.text = data[position].msg ?: "Lets have a chat!"
        if (data[position].timestamp != null)
            userTimestamp.text = DateUtils.getRelativeTimeSpanString(data[position].timestamp!!.toLong())
        else
            userTimestamp.text = ""

        Glide.with(holder.itemView)
            .load(data[position].profileUrl)
            .placeholder(R.drawable.ic_baseline_person_24)
            .centerCrop()
            .into(userImg)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(data[position])
        }
    }

    override fun getItemCount(): Int = data.size
}