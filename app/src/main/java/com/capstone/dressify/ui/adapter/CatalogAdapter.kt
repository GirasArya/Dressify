package com.capstone.dressify.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.dressify.data.remote.response.CatalogResponse
import com.capstone.dressify.databinding.ItemCardBinding

class CatalogAdapter(private val productList: List<CatalogResponse>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(product: CatalogResponse)
    }

    class CatalogViewHolder(private val binding: ItemCardBinding, private val itemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: CatalogResponse) {
            Glide.with(itemView)
                .load(product.image)
                .into(binding.ivImgClothes)
            binding.tvClotheName.text = product.title
            itemView.setOnClickListener {
                itemClickListener.onItemClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogViewHolder(binding, itemClickListener)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        holder.bind(productList[position])
    }
}