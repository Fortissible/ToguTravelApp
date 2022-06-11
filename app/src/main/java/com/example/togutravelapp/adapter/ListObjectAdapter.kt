package com.example.togutravelapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.togutravelapp.R
import com.example.togutravelapp.data.DummyObjectData
import com.example.togutravelapp.data.ObjectWisataResponseItem

class ListObjectAdapter(private val listObject: List<ObjectWisataResponseItem>): RecyclerView.Adapter<ListObjectAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    interface OnItemClickCallback{
        fun onItemClicked(data:ObjectWisataResponseItem)
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val objectPic : ImageView = itemView.findViewById(R.id.location_detail_object_img)
        val objectTitle : TextView = itemView.findViewById(R.id.location_detail_object_title)
        val objectDesc : TextView = itemView.findViewById(R.id.location_detail_object_desc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.location_detail_object_item_card,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val objectDesc = holder.objectDesc
        val objectTitle = holder.objectTitle
        val objectPic = holder.objectPic
        objectDesc.text =  listObject[position].deskripsi
        objectTitle.text = listObject[position].nama
        Glide.with(holder.itemView)
            .load(listObject[position].urlFotoObjek)
            .placeholder(R.drawable.ios_android_free)
            .centerCrop()
            .into(objectPic)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listObject[position])
        }
    }

    override fun getItemCount(): Int = listObject.size
}