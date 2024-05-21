package com.capstone.dressify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.dressify.databinding.ItemCardBinding
import com.capstone.dressify.response.Response

class CatalogAdapter(private val productList: List<Response>): RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder>() {
    class CatalogViewHolder(private val binding: ItemCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Response) {
            Glide.with(itemView)
                .load(product.image)
                .into(binding.ivImgClothes)
            binding.tvClotheName.text = product.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogViewHolder(binding)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        holder.bind(productList[position])
    }


}