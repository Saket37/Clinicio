package com.example.clinicio.data.remote.slot

import com.squareup.moshi.Json

data class BookAppointment(
    @field:Json(name = "id") val id: Int = 0,
    @field:Json(name = "slot_id") val slotId: Int = 0,
    @field:Json(name = "booked_by") val bookedId: Int = 0,
    @field:Json(name = "reason") val reason: String,
    @field:Json(name = "notes") val note: String? = null,
)
