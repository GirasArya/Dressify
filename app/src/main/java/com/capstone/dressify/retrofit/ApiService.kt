package com.capstone.dressify.retrofit

import com.capstone.dressify.response.Response
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    fun getProducts(): Call<List<Response>>
}