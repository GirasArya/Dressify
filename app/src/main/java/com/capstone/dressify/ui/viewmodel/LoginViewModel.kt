package com.capstone.dressify.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.dressify.data.remote.response.LoginResponse
import com.capstone.dressify.domain.model.User
import com.capstone.dressify.domain.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse


    //Post user credential for login
    suspend fun login(email: String, password: String) {
        val response = repository.login(email, password)
        _loginResponse.postValue(response)
    }


    //save user session
    suspend fun saveSession(user: User) {
        repository.saveSession(user)
    }

    //check user login session state
    fun loginSession() {
        viewModelScope.launch {
            repository.isLoggedIn()
        }
    }

    //get session
    fun getSession(): LiveData<User> {
        return repository.getSession()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }


}