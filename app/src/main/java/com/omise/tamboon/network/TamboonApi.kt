package com.omise.tamboon.network

import com.omise.tamboon.model.Charity
import com.omise.tamboon.model.DonationRequest
import com.omise.tamboon.model.DonationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

const val TAMBON_BASE_URL: String = "http://10.0.2.2:8080/"

interface TamboonApi {
    @GET("charities")
    suspend fun getCharities(): List<Charity>

    @POST("donations")
    suspend fun donate(@Body donationRequest: DonationRequest): DonationResponse
}