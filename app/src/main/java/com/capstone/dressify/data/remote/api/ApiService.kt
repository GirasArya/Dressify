package com.capstone.dressify.data.remote.api


import com.capstone.dressify.data.remote.response.CatalogResponse
import com.capstone.dressify.data.remote.response.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("products")
    fun getProducts():Call<List<CatalogResponse>>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse
}