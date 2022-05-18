package com.example.togutravelapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.togutravelapp.R
import com.example.togutravelapp.data.DummyRecommendData

class ListRecommendationAdapter(private val listRecom : List<DummyRecommendData>):RecyclerView.Adapter<ListRecommendationAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    interface OnItemClickCallback{
        fun onItemClicked(data:DummyRecommendData)
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val recomImg : ImageView = itemView.findViewById(R.id.location_detail_recom_img)
        val recomTitle : TextView = itemView.findViewById(R.id.location_detail_recom_title)
        val recomDesc : TextView = itemView.findViewById(R.id.location_detail_recom_desc)
        val recomPrice : TextView = itemView.findViewById(R.id.location_detail_recom_price)
        val recomLoc : TextView = itemView.findViewById(R.id.location_detail_recom_loc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val item : View = LayoutInflater.from(parent.context).inflate(R.layout.location_detail_recom_item_card,parent,false)
        return ListViewHolder(item)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val recomImg = holder.recomImg
        val recomTitle = holder.recomTitle
        val recomDesc = holder.recomDesc
        val recomPrice = holder.recomPrice
        val recomLoc = holder.recomLoc

        recomDesc.text = listRecom[position].recomDesc
        recomPrice.text = listRecom[position].recomPrice
        recomTitle.text = listRecom[position].recomTitle
        recomLoc.text = listRecom[position].recomLoc

        Glide.with(holder.itemView)
            .load(listRecom[position].recomUrl)
            .centerCrop()
            .into(recomImg)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listRecom[position])
        }
    }

    override fun getItemCount(): Int = listRecom.size
}