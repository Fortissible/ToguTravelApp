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

class ListObjectAdapter(private val listObject: List<DummyObjectData>): RecyclerView.Adapter<ListObjectAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    interface OnItemClickCallback{
        fun onItemClicked(data:DummyObjectData)
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
        objectDesc.text =  listObject[position].objectDesc
        objectTitle.text = listObject[position].objectTitle
        Glide.with(holder.itemView)
            .load(listObject[position].objectUrl)
            .centerCrop()
            .into(objectPic)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listObject[position])
        }
    }

    override fun getItemCount(): Int = listObject.size
}