package com.omise.tamboon.replacement

import com.omise.tamboon.model.Token
import com.omise.tamboon.testutils.JsonParser
import okhttp3.RequestBody

var URL_TOKEN = ""

class OmiseApi : com.omise.tamboon.network.OmiseApi {
    override suspend fun getToken(requestBody: RequestBody): Token {
        return JsonParser.getUrl<Token>(URL_TOKEN) ?: Token("")
    }
}