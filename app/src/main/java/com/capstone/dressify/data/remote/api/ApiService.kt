package com.capstone.dressify.data.remote.api


import com.capstone.dressify.data.remote.response.CatalogResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    fun getProducts(): Call<List<CatalogResponse>>
}