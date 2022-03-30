package com.example.clinicio.data.remote.user.entity

data class LoginResponse(
    val token: String,
    val user: User,
)