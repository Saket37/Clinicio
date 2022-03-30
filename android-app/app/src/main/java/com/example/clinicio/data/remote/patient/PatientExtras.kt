package com.example.clinicio.data.remote.patient

import com.squareup.moshi.Json

data class PatientExtras(
    @field:Json(name = "id") val id: Int = 0,
    @field:Json(name = "blood_group") val blood_group: String? = null,
    @field:Json(name = "weight") val weight: String? = null,
    @field:Json(name = "patient_id") val patient_id: Int? = null,
    @field:Json(name = "created_at") val created_at: String? = null,
    @field:Json(name = "updated_at") val updated_at: String? = null,
)
