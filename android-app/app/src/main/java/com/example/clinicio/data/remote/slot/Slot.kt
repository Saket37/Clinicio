package com.example.clinicio.data.remote.slot

import com.squareup.moshi.Json

data class Slot(
    @field:Json(name = "id") val id: Int = 0,
    @field:Json(name = "start_time") val start_time: String,
    @field:Json(name = "end_time") val end_time: String,
    @field:Json(name = "avail_id") val avail_id: Int,
    @field:Json(name = "created_at") val created_at: String? = null,
    @field:Json(name = "updated_at") val updated_at: String? = null,
    @field:Json(name = "status") val status: Status,
)
