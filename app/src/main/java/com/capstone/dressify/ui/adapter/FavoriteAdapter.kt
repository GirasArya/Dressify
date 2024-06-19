package com.capstone.dressify.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.dressify.data.local.database.FavoriteEntity
import com.capstone.dressify.databinding.ItemCardBinding
import com.capstone.dressify.helpers.FavoriteItemCallback
import com.capstone.dressify.ui.view.camera.CameraActivity
import com.capstone.dressify.ui.viewmodel.FavoriteViewModel

@Suppress("DEPRECATION")
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


    inner class FavoriteViewHolder(val binding: ItemCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FavoriteEntity, favoriteViewModel: FavoriteViewModel) {
            Glide.with(binding.ivImgClothes)
                .load(item.image)
                .into(binding.ivImgClothes)
            binding.tvClotheName.text = item.title
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(item, adapterPosition)
            }


            binding.ivIcFavorite.setOnClickListener {
                favoriteViewModel.deleteFavorite(item.title, item.image.toString())
            }

            binding.flCamera.setOnClickListener {
                val imageItem = item.image
                val intent = Intent(itemView.context, CameraActivity::class.java)
                intent.putExtra("IMAGE_URL", imageItem.toString())
                startActivity(itemView.context, intent, null)
            }

            // Set the initial toggle state
            binding.ivIcFavorite.isChecked = item.isCheck

            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(item, adapterPosition)
            }

//            itemView.setOnClickListener {
//                val intent = Intent(itemView.context, CameraActivity::class.java)
//               startActivity(itemView.context, intent, null)
//            }

            binding.flCamera.setOnClickListener {
                val intent = Intent(itemView.context, CameraActivity::class.java)
                startActivity(itemView.context, intent, null)
            }

            binding.ivIcFavorite.setOnClickListener {
                notifyItemChanged(adapterPosition) // Notify the adapter to update this item
                if (item.isCheck) {
                    favoriteViewModel.deleteFavorite(item.title, item.image.toString())
                    favoriteViewModel.deleteFavorite(item.title, item.image.toString())
                }
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