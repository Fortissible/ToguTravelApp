package com.example.togutravelapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.togutravelapp.R
import com.example.togutravelapp.data.TourguideItem
import de.hdodenhof.circleimageview.CircleImageView

class ListTourGuideAdapter(private val listTogu: List<TourguideItem>) :RecyclerView.Adapter<ListTourGuideAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    interface OnItemClickCallback{
        fun onItemClicked(data:TourguideItem)
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tgImg : CircleImageView = itemView.findViewById(R.id.tg_img)
        val tgName : TextView = itemView.findViewById(R.id.tg_name)
        val tgGender : TextView = itemView.findViewById(R.id.tg_gen)
        val tgRating : TextView = itemView.findViewById(R.id.tg_rat)
        val tgPrice : TextView = itemView.findViewById(R.id.tg_price)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val item : View = LayoutInflater.from(parent.context).inflate(R.layout.list_tourguide_item_card,parent,false)
        return ListViewHolder(item)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val tgImg = holder.tgImg
        val tgName = holder.tgName
        val tgGender = holder.tgGender
        val tgRating = holder.tgRating
        val tgPrice = holder.tgPrice
        var price = 0
        var rating = 0

        if (listTogu[position].rating == null) {
            rating = 0
        }
        if (listTogu[position].harga == null || listTogu[position].harga == 0) {
            price = 15000
        }

        tgName.text = listTogu[position].nama
        tgGender.text = listTogu[position].gender
        tgRating.text = rating.toString()
        tgPrice.text = price.toString()

        Glide.with(holder.itemView)
            .load(listTogu[position].urlImage)
            .placeholder(R.drawable.propict)
            .centerCrop()
            .into(tgImg)


        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listTogu[position])
        }
    }

    override fun getItemCount(): Int = listTogu.size
}