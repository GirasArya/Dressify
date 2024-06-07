package com.capstone.dressify.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.dressify.R

class LandingAdapter(
    private var title: List<String>,
    private var description: List<String>,
    private var image: List<Int>
) : RecyclerView.Adapter<LandingAdapter.ViewPagerViewHolder>() {
    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageTitle = itemView.findViewById<ImageView>(R.id.iv_item_landing)
        val textTitle = itemView.findViewById<TextView>(R.id.tv_landing_title)
        val textDesc = itemView.findViewById<TextView>(R.id.tv_landing_description)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LandingAdapter.ViewPagerViewHolder {
        return ViewPagerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_landing, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LandingAdapter.ViewPagerViewHolder, position: Int) {
        holder.imageTitle.setImageResource(image[position])
        holder.textTitle.text = title[position]
        holder.textDesc.text = description[position]
    }

    override fun getItemCount(): Int {
        return title.size
    }
}