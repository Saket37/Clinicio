package com.example.clinicio.data.remote.patient

import com.squareup.moshi.Json

data class UpcomingAppointment(
    @field:Json(name = "slot_id") var slot_id: Int,
    @field:Json(name = "name") var name: String,
    @field:Json(name = "speciality") val speciality: String? = null,
    @field:Json(name = "degree") val degree: String? = null,
    @field:Json(name = "start_time") val start_time: String,
    @field:Json(name = "reason") val reason: String,
)
