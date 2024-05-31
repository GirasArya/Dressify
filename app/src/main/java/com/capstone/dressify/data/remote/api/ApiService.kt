package com.capstone.dressify.data.remote.api


import com.capstone.dressify.data.remote.response.CatalogResponse
import com.capstone.dressify.data.remote.response.LoginResponse
import com.capstone.dressify.data.remote.response.RegisterResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("products")
    fun getProducts(): Call<List<CatalogResponse>>

    @POST("login")
    suspend fun login(
        @Body raw: JsonObject
    ) : LoginResponse

    @POST("register")
    suspend fun register(
        @Body raw: JsonObject
    ): RegisterResponse
}