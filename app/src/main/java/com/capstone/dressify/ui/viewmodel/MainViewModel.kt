package com.capstone.dressify.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.dressify.data.remote.api.ApiConfig
import com.capstone.dressify.data.remote.response.CatalogResponse
import com.capstone.dressify.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _productList = MutableLiveData<List<CatalogResponse>>()
    val productList: LiveData<List<CatalogResponse>> get() = _productList
    val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchProducts() {
        viewModelScope.launch {
            _isLoading.value = true // Show loading indicator
            try {
                val productList = repository.getProductCatalog()
                _productList.value = productList
            } catch (e: Exception) {
                // Handle errors (e.g., network issues, parsing errors)
                Log.e("MainViewModel", "Error fetching products: ${e.message}")
            } finally {
                _isLoading.value = false // Hide loading indicator (Modify if success)
            }
        }
    }
}