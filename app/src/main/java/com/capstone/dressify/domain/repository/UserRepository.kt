package com.capstone.dressify.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.capstone.dressify.data.local.datastore.UserPreference
import com.capstone.dressify.data.remote.api.ApiService
import com.capstone.dressify.domain.model.User

class UserRepository constructor(
    private val apiService: ApiService,
    private val pref: UserPreference
) {
    fun getSession(): LiveData<User> {
        return pref.getToken().asLiveData()
    }

    suspend fun saveSession(user: User) {
        return pref.saveUserToken(user)
    }

    suspend fun login() {
        return pref.isLogin()
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