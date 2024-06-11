package com.capstone.dressify.data.remote.api


import com.capstone.dressify.data.remote.response.CatalogResponse
import com.capstone.dressify.data.remote.response.LoginResponse
import com.capstone.dressify.data.remote.response.RegisterResponse
import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("list")
    suspend fun getProducts(
        @Query("page") page: Int = 1, // Default to page 1
        @Query("size") size: Int = 10 // Default to 10 items per page
    ): CatalogResponse

    @POST("login")
    suspend fun login(
        @Body raw: JsonObject
    ): LoginResponse

    @POST("register")
    suspend fun register(
        @Body raw: JsonObject
    ): RegisterResponse
}