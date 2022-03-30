package com.example.clinicio.data.remote.doctor

import com.squareup.moshi.Json

data class DoctorExtras(
    @field:Json(name = "id") val id: Int = 0,
    @field:Json(name = "speciality") val speciality: String? = null,
    @field:Json(name = "degree") val degree: String? = null,
    @field:Json(name = "doctor_id") val doctor_id: Int? = null,
    @field:Json(name = "created_at") val createdAt: String? = null,
    @field:Json(name = "updated_at") val updatedAt: String? = null,
)
