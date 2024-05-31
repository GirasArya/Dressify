package com.capstone.dressify.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.dressify.data.remote.response.RegisterResponse
import com.capstone.dressify.domain.repository.UserRepository

class RegisterViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    suspend fun registerUser(email: String, username: String, password: String) {
        val response = userRepository.register(email, username, password)
        _registerResponse.value = response
    }
}