package com.capstone.dressify.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.dressify.data.local.FavoriteEntity
import com.capstone.dressify.databinding.ItemCardBinding
import com.capstone.dressify.helpers.FavoriteItemCallback
import com.capstone.dressify.ui.viewmodel.FavoriteViewModel

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

   lateinit var favoriteViewModel: FavoriteViewModel

    private val listFavItem = ArrayList<FavoriteEntity>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setListAdapter(listFav: List<FavoriteEntity>) {
        val diffCallback = FavoriteItemCallback(this.listFavItem, listFav)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavItem.clear()
        this.listFavItem.addAll(listFav)
        diffResult.dispatchUpdatesTo(this)
    }


    inner class FavoriteViewHolder(private val binding: ItemCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FavoriteEntity, favoriteViewModel: FavoriteViewModel) {
            Glide.with(binding.ivImgClothes)
                .load(item.image)
                .into(binding.ivImgClothes)
            binding.tvClotheName.text = item.title
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(item, adapterPosition)
            }
            binding.ivIcFavorite.setOnClickListener {
                favoriteViewModel.deleteFavorite(item.title ?: "", item.image ?: "")
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(favorite: FavoriteEntity, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listFavItem.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavItem[position], favoriteViewModel)
    }
}