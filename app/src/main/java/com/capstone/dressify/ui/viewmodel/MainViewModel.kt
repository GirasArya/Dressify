package com.capstone.dressify.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.dressify.data.remote.response.CatalogResponse
import com.capstone.dressify.domain.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _productList = MutableLiveData<CatalogResponse>()
    val productList: LiveData<CatalogResponse> get() = _productList
    val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchProducts() {
        viewModelScope.launch {
            _isLoading.value = true // Show loading indicator
            try {
                val catalogResponse = repository.getProductCatalog()
                _productList.value = catalogResponse
            } catch (e: Exception) {
                // Handle errors (e.g., network issues, parsing errors)
                Log.e("MainViewModel", "Error fetching products: ${e.message}")
            } finally {
                _isLoading.value = false // Hide loading indicator (Modify if success)
            }
        }
    }
}

