package com.example.clinicio.data.remote.doctor

import com.example.clinicio.data.remote.user.entity.Role
import com.squareup.moshi.Json

data class Doctor(
    @field:Json(name = "id") val id: Int = 0,
    @field:Json(name = "name") var name: String?,
    @field:Json(name = "email") var email: String,
    @field:Json(name = "phone") var phone: String,
    @field:Json(name = "address") var address: String,
    @field:Json(name = "role") val role: Role,
    @field:Json(name = "created_at") val createdAt: String? = null,
    @field:Json(name = "updated_at") val updatedAt: String? = null,
    @field:Json(name = "doctor_extras") val doctor_extras: DoctorExtras?,
)
