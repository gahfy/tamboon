package com.omise.tamboon.testutils

import com.squareup.moshi.Moshi
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

class JsonParser {
    companion object {
        @JvmStatic
        inline fun <reified T> getUrl(jsonPath: String): T? {
            val buildClassPath =
                JsonParser::class.java.protectionDomain?.codeSource?.location?.path ?: ""
            val assetsPath = buildClassPath.replace(
                "/build/tmp/kotlin-classes/debugUnitTest",
                "/src/test/assets/api_mocks/$jsonPath"
            )

            val buf = StringBuilder()
            val inputStream = FileInputStream(assetsPath)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var str: String? = bufferedReader.readLine()
            while (str != null) {
                buf.append(str)
                str = bufferedReader.readLine()
            }
            inputStream.close()
            bufferedReader.close()

            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(T::class.java)
            return jsonAdapter.fromJson(buf.toString())
        }
    }
}