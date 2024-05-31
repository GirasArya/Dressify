package com.capstone.dressify.domain.model

data class User(
    val username : String,
    val email : String,
    val token : String,
    val isLoggedIn : Boolean
)