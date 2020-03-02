package com.omise.tamboon.replacement

import com.omise.tamboon.model.Charity
import com.omise.tamboon.model.DonationRequest
import com.omise.tamboon.model.DonationResponse
import com.omise.tamboon.testutils.JsonParser

var URL_CHARITIES = ""

class TamboonApi : com.omise.tamboon.network.TamboonApi {
    override suspend fun getCharities(): List<Charity> {
        return JsonParser.getUrl<List<Charity>>(URL_CHARITIES) ?: listOf()
    }

    override suspend fun donate(donationRequest: DonationRequest): DonationResponse {
        return DonationResponse(false, "", "")
    }

}