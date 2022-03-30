package com.example.clinicio.data.remote.availability

import com.squareup.moshi.Json

data class Availability(
    @field:Json(name = "id") val id: Int = 0,
    @field:Json(name = "day") val day: String,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "start_time") val start_time: String,
    @field:Json(name = "end_time") val end_time: String,
    @field:Json(name = "interval") val interval: Int,
    @field:Json(name = "doc_id") val doc_id: Int,
    @field:Json(name = "updated_at") val updated_at: String? = null,
    @field:Json(name = "created_at") val created_at: String? = null,

    )