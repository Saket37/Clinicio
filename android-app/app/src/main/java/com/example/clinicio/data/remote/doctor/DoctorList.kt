package com.example.clinicio.data.remote.doctor

import com.squareup.moshi.Json

data class DoctorList(
    @field:Json(name = "id") val id: Int = 0,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "speciality") val speciality: String,
    @field:Json(name = "degree") val degree: String,
)
