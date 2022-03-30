package com.example.clinicio.data.remote.patient

import com.squareup.moshi.Json

data class AppointmentHistory(
    @field:Json(name = "name") var name: String,
    @field:Json(name = "phone") var phone: String,
    @field:Json(name = "speciality") val speciality: String? = null,
    @field:Json(name = "degree") val degree: String? = null,
    @field:Json(name = "start_time") val start_time: String,
    @field:Json(name = "reason") val reason: String,
)
