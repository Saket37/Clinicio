package com.example.clinicio.data.remote.availability

import com.squareup.moshi.Json

data class AvailabilityTime(
    @field:Json(name = "start_time") val start_time: String,
    @field:Json(name = "end_time") val end_time: String,
)
