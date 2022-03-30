package com.example.clinicio.data.remote.entity

data class APIError(
    val message: String,
    val fields: List<Map<String, String>>?
)