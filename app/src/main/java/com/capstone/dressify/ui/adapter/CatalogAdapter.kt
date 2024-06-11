package com.capstone.dressify.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.dressify.data.remote.response.ClothingItemsItem
import com.capstone.dressify.databinding.ItemCardBinding
import com.capstone.dressify.ui.view.camera.CameraActivity
import com.capstone.dressify.ui.viewmodel.FavoriteViewModel

class CatalogAdapter(
    private var productList: List<ClothingItemsItem>,
    private val favoriteViewModel: FavoriteViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val onFavoriteClickListener: OnFavoriteClickListener
) : RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder>() {
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

            val title: String? = product.productDisplayName
            val image: String? = product.productDisplayName

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
                    favoriteViewModel.addFavorite(title!!, image!!)
                } else {
                    favoriteViewModel.deleteFavorite(title!!, image!!)
                }
                binding.ivIcFavorite.isChecked = isCheck
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogViewHolder(binding)
    }

    fun updateProductList(newProducts: List<ClothingItemsItem>) {
        productList = newProducts
        notifyDataSetChanged() // Refresh the adapter
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    interface OnFavoriteClickListener {
        fun onFavoriteClick(product: ClothingItemsItem)
    }
}