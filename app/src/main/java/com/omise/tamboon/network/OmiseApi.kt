package com.omise.tamboon.network

import com.omise.tamboon.model.Token
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

const val OMISE_BASE_URL: String = "https://vault.omise.co/"

interface OmiseApi{
    @POST("tokens")
    suspend fun getToken(@Body requestBody: RequestBody): Token
}