package com.example.clinicio.data.remote.slot

import com.squareup.moshi.Json

data class SlotInfo(
    @field:Json(name = "start_time") val start_time: String,
    @field:Json(name = "name") val name: String,
)
