package com.example.clinicio.data.remote.user.entity

import com.squareup.moshi.Json

data class Login(
    @field:Json(name = "email") val email: String,
    @field:Json(name = "password") val password: String,
    @field:Json(name = "device_name") var device_name: String,
)
