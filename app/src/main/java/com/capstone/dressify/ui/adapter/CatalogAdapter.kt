package com.capstone.dressify.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.dressify.R
import com.capstone.dressify.data.remote.response.CatalogResponse
import com.capstone.dressify.databinding.ItemCardBinding
import com.capstone.dressify.ui.view.camera.CameraActivity
import com.capstone.dressify.ui.viewmodel.FavoriteViewModel

class CatalogAdapter(
    private val productList: List<CatalogResponse>,
    private val favoriteViewModel: FavoriteViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val onFavoriteClickListener: OnFavoriteClickListener
) : RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder>() {

    inner class CatalogViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: CatalogResponse) {
            Glide.with(itemView)
                .load(product.image)
                .into(binding.ivImgClothes)
            binding.tvClotheName.text = product.title

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, CameraActivity::class.java)
                startActivity(itemView.context, intent, null)
            }

            binding.ivIcFavorite.setOnClickListener {
                onFavoriteClickListener.onFavoriteClick(product)
            }

            favoriteViewModel.isItemFavorite(product.title ?: "").observe(lifecycleOwner) { isFavorite ->
                if (isFavorite) {
                    binding.ivIcFavorite.setImageResource(R.drawable.ic_favorite_fill)
                } else {
                    binding.ivIcFavorite.setImageResource(R.drawable.ic_favorite_outline)
                }
            }
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

    interface OnFavoriteClickListener {
        fun onFavoriteClick(product: CatalogResponse)
    }
}