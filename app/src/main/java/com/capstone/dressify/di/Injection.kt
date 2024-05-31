package com.capstone.dressify.di

import android.content.Context
import com.capstone.dressify.data.local.datastore.UserPreference
import com.capstone.dressify.data.local.datastore.dataStore
import com.capstone.dressify.data.remote.api.ApiConfig
import com.capstone.dressify.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getToken().first() }
        val apiService = ApiConfig.getApiService(user.token) //get user token jangan lupa
        return UserRepository.getInstance(apiService, pref)
    }
}