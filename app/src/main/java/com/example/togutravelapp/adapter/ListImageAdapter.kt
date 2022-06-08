package com.example.togutravelapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.togutravelapp.R
import com.example.togutravelapp.data.DummyURL

class ListImageAdapter(private val listImageLink: List<DummyURL>):RecyclerView.Adapter<ListImageAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback:OnItemClickCallback
    interface OnItemClickCallback{
        fun onItemClicked()
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imagePic : ImageView = itemView.findViewById(R.id.item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.location_detail_image_item,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val profilepic = listImageLink[position].urlImage
        Glide.with(holder.itemView)
            .load(profilepic)
            .centerCrop()
            .into(holder.imagePic)
        /*
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listImageLink[holder.adapterPosition])
        }*/
    }

    override fun getItemCount(): Int = listImageLink.size
}