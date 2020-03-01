package com.omise.tamboon.model

import com.squareup.moshi.Json

data class DonationResponse(
    @field:Json(name="success")
    val success: Boolean,
    @field:Json(name="error_code")
    val errorCode: String,
    @field:Json(name="error_message")
    val errorMessage: String
)