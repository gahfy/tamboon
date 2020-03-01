package com.omise.tamboon.model

import com.squareup.moshi.Json

data class Token(
    @field:Json(name="id")
    val id: String
)