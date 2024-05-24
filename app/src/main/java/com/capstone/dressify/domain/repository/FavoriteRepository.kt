package com.capstone.dressify.domain.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.capstone.dressify.data.local.FavoriteDao
import com.capstone.dressify.data.local.FavoriteEntity
import com.capstone.dressify.data.local.FavoriteRoomDatabase

class FavoriteRepository(application: Application) {
    private val favoriteDao: FavoriteDao

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        favoriteDao = db.favoriteDao()
    }

    fun getAllFavorite(): LiveData<List<FavoriteEntity>> {
        return favoriteDao.getAllFavorite()
    }
}