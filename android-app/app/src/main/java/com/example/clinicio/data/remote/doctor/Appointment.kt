package com.example.clinicio.data.remote.doctor

import com.squareup.moshi.Json


data class Appointment(
    @field:Json(name = "name") var name: String,
    @field:Json(name = "phone") var phone: String,
    @field:Json(name = "address") var address: String,
    @field:Json(name = "blood_group") val blood_group: String? = null,
    @field:Json(name = "weight") val weight: Int? = null,
    @field:Json(name = "start_time") val start_time: String,
    @field:Json(name = "reason") val reason: String,
    @field:Json(name = "notes") val note: String? = null,

    )
