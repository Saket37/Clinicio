package com.example.clinicio.data.remote.user.entity

import com.squareup.moshi.Json

data class User(
    @field:Json(name = "id") var id: Int = 0,
    @field:Json(name = "name") var name: String?,
    @field:Json(name = "email") var email: String,
    @field:Json(name = "password") var password: String,
    @field:Json(name = "phone") var phone: String,
    @field:Json(name = "address") var address: String,
    @field:Json(name = "role") val role: Role,
    @field:Json(name = "created_at") val createdAt: String? = null,
    @field:Json(name = "updated_at") val updatedAt: String? = null,
)