package com.example.clinicio.data.remote.availability

import com.squareup.moshi.Json

data class AvailabilityDate(
    @field:Json(name = "date") val date: String,
)
