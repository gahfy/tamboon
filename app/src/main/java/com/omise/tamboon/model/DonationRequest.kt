package com.omise.tamboon.model

import com.squareup.moshi.Json

data class DonationRequest(
    @field:Json(name="name")
    val name: String,
    @field:Json(name="token")
    val token: String,
    @field:Json(name="amount")
    val amount: Int
)