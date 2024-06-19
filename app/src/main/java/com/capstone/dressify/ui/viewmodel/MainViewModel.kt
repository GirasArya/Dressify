package com.capstone.dressify.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.dressify.data.remote.response.ClothingItemsItem
import com.capstone.dressify.domain.repository.UserRepository

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    suspend fun fetchProducts(): LiveData<PagingData<ClothingItemsItem>> {
        _isLoading.value = true
        val products = repository.getProductCatalog().cachedIn(viewModelScope)
        _isLoading.value = false
        return products
    }
}
