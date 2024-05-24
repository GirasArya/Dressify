package com.capstone.dressify.helpers

import androidx.recyclerview.widget.DiffUtil
import com.capstone.dressify.data.local.FavoriteEntity

class FavoriteItemCallback(private val oldFavlist: List<FavoriteEntity>, private val newFavList: List<FavoriteEntity>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldFavlist.size
    }

    override fun getNewListSize(): Int {
        return newFavList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavlist[oldItemPosition].title == newFavList[newItemPosition].title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFav = oldFavlist[oldItemPosition]
        val newFav = newFavList[newItemPosition]
        return oldFav.title == newFav.title && oldFav.image == newFav.image
    }

}