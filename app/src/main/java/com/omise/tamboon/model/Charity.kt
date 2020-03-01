package com.omise.tamboon.model

import com.squareup.moshi.Json

data class Charity(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "logo_url")
    val logoUrl: String
)