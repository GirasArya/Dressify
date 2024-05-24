package com.capstone.dressify.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.capstone.dressify.data.local.FavoriteDao
import com.capstone.dressify.data.local.FavoriteEntity
import com.capstone.dressify.data.local.FavoriteRoomDatabase
import com.capstone.dressify.domain.repository.FavoriteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application): ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)
    private var favoriteDao: FavoriteDao
    private var db: FavoriteRoomDatabase = FavoriteRoomDatabase.getDatabase(application)
    var itemFavorite: Boolean = false
    var _isLoading = MutableLiveData<Boolean>()
    var isLoading : LiveData<Boolean> = _isLoading

    init {
        favoriteDao = db.favoriteDao()
        _isLoading.value = true
    }


    fun getAllFavorite(): LiveData<List<FavoriteEntity>> {
        _isLoading.value = false
        return mFavoriteRepository.getAllFavorite()
    }

    fun addFavorite(title: String, image: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val item = FavoriteEntity(title, image, isCheck = true)
            favoriteDao.addFavorite(item)
            itemFavorite = true
        }
    }

    fun deleteFavorite(title: String, image: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val item = FavoriteEntity(title, image, isCheck = false)
            favoriteDao.deleteFavorite(item)
            itemFavorite = false
        }
    }

    fun isItemFavorite(title: String): LiveData<Boolean> {
        return favoriteDao.getFavoriteByTitle(title).map { it.isNotEmpty() }
    }

}