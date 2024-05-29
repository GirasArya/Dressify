package com.capstone.dressify.domain.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.capstone.dressify.data.local.datastore.UserPreference
import com.capstone.dressify.data.remote.api.ApiConfig
import com.capstone.dressify.data.remote.api.ApiService
import com.capstone.dressify.data.remote.response.CatalogResponse
import com.capstone.dressify.data.remote.response.LoginResponse
import com.capstone.dressify.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository constructor(
    private val apiService: ApiService,
    private val pref: UserPreference
) {
    val _productList = MutableLiveData<List<CatalogResponse>>()
    val productList : LiveData<List<CatalogResponse>>get()  = _productList

    val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading


    suspend fun getProductCatalog(): List<CatalogResponse> = withContext(Dispatchers.IO) {
        val response = apiService.getProducts().execute()

        if (response.isSuccessful) {
            response.body() ?: emptyList() // Return the list or an empty list if null
        } else {
            throw Exception("Failed to fetch products: ${response.code()}") // Throw an exception on failure
        }
    }



    fun getSession(): LiveData<User> {
        return pref.getToken().asLiveData()
    }

    suspend fun saveSession(user: User) {
        return pref.saveUserToken(user)
    }

    suspend fun isLoggedIn() {
        return pref.isLogin()
    }

    suspend fun login(email: String, password: String) : LoginResponse{
        return apiService.login(email, password)
    }

    suspend fun logout() {
        return pref.clearUserToken()
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository {
            return instance ?: synchronized(this) {
                instance ?: UserRepository(
                    apiService,
                    userPreference
                ).also { instance = it }
            }
        }
    }
}