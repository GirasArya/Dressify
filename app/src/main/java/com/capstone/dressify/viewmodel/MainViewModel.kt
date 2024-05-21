package com.capstone.dressify.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.dressify.response.Response
import com.capstone.dressify.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback

class MainViewModel: ViewModel() {
    val productList = MutableLiveData<List<Response>>()

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        val client = ApiConfig.getApiService().getProducts()
        client.enqueue(object : Callback<List<Response>> {
            override fun onResponse(
                call: Call<List<Response>>,
                response: retrofit2.Response<List<Response>>
            ) {
                if (response.isSuccessful) {
                    productList.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<Response>>, t: Throwable) {
                Log.e("MainViewModel", "OnFailure: ${t.message.toString()}")
            }

        })
    }
}