package com.capstone.dressify.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.dressify.data.remote.api.ApiConfig
import com.capstone.dressify.data.remote.response.CatalogResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    val productList = MutableLiveData<List<CatalogResponse>>()
    val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private var _isToggleChecked = MutableStateFlow(false)
    var isToggleChecked: StateFlow<Boolean> = _isToggleChecked

    fun fetchProducts() {
        val client = ApiConfig.getApiService().getProducts()
        _isLoading.value = true
        client.enqueue(object : Callback<List<CatalogResponse>> {
            override fun onResponse(
                call: Call<List<CatalogResponse>>,
                response: Response<List<CatalogResponse>>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    productList.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<CatalogResponse>>, t: Throwable) {
                Log.e("THIS", "onFailure: ${t.message}")
            }
        })
    }


}