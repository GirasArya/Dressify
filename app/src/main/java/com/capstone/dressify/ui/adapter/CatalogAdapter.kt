package com.capstone.dressify.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.dressify.R
import com.capstone.dressify.data.remote.response.ClothingItemsItem
import com.capstone.dressify.databinding.ItemCardBinding
import com.capstone.dressify.ui.view.camera.CameraActivity
import com.capstone.dressify.ui.viewmodel.FavoriteViewModel

class CatalogAdapter(
    private var productList: List<ClothingItemsItem>,
    private val favoriteViewModel: FavoriteViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val onFavoriteClickListener: OnFavoriteClickListener
) : PagingDataAdapter<ClothingItemsItem, CatalogAdapter.CatalogViewHolder>(DIFF_CALLBACK) {
    private val isFavoriteLiveData = MutableLiveData<Boolean>()

    inner class CatalogViewHolder(val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ClothingItemsItem) {
            Glide.with(itemView)
                .load(product.pictureLink)
                .into(binding.ivImgClothes)
            binding.tvClotheName.text = product.productDisplayName

            binding.flCamera.setOnClickListener {
                val imageItem = product.pictureLink
                val intent = Intent(itemView.context, CameraActivity::class.java)
                intent.putExtra("IMAGE_URL", imageItem)
                startActivity(itemView.context, intent, null)
            }


            binding.ivIcFavorite.setOnClickListener {
                onFavoriteClickListener.onFavoriteClick(product)
            }

            val title: String = product.productDisplayName ?: ""
            val image: String = product.pictureLink ?: ""

            var isCheck = false
            favoriteViewModel.isItemFavorite(product.productDisplayName ?: "")
                .observe(lifecycleOwner) { isFavorite ->
                    if (isFavorite) {
                        binding.ivIcFavorite.isChecked = true
                        isCheck = true
                    } else {
                        binding.ivIcFavorite.isChecked = false
                        isCheck = false
                    }
                }


            binding.ivIcFavorite.setOnClickListener {
                isCheck = !isCheck
                if (isCheck) {
                    favoriteViewModel.addFavorite(title, image)
                } else {
                    favoriteViewModel.deleteFavorite(title, image)
                }
                binding.ivIcFavorite.isChecked = isCheck
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogViewHolder(binding)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        } else {
            // Placeholder Content
            holder.binding.ivImgClothes.setImageResource(R.drawable.ic_place_holder)
            holder.binding.tvClotheName.text = "Loading..."
            // You can also disable any interactive elements here
            holder.binding.flCamera.isEnabled = false
            holder.binding.ivIcFavorite.isEnabled = false
        }
    }
    interface OnFavoriteClickListener {
        fun onFavoriteClick(product: ClothingItemsItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ClothingItemsItem>() {
            override fun areItemsTheSame(
                oldItem: ClothingItemsItem,
                newItem: ClothingItemsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ClothingItemsItem,
                newItem: ClothingItemsItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}