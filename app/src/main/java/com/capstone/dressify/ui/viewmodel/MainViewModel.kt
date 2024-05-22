package com.capstone.dressify.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.dressify.data.remote.response.CatalogResponse
import com.capstone.dressify.data.remote.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback

class MainViewModel: ViewModel() {
    val productList = MutableLiveData<List<CatalogResponse>>()

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        val client = ApiConfig.getApiService().getProducts()
        client.enqueue(object : Callback<List<CatalogResponse>> {
            override fun onResponse(
                call: Call<List<CatalogResponse>>,
                response: retrofit2.Response<List<CatalogResponse>>
            ) {
                if (response.isSuccessful) {
                    productList.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<CatalogResponse>>, t: Throwable) {
                Log.e("MainViewModel", "OnFailure: ${t.message.toString()}")
            }

        })
    }
}