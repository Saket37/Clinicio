package com.example.clinicio.data.remote.entity

import com.squareup.moshi.Json
import java.util.*

open class Model(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "created_at") val createdAt: Date,
    @field:Json(name = "updated_at") val updatedAt: Date,
)