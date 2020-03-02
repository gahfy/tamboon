package com.omise.tamboon.replacement

import com.omise.tamboon.BuildConfig
import com.omise.tamboon.network.OMISE_BASE_URL
import com.omise.tamboon.network.OmiseApi
import com.omise.tamboon.network.TamboonApi
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val testNetworkModule = module {
    single<TamboonApi> {
        TamboonApi()
    }

    single<OmiseApi> {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request: Request = chain.request()
                    val credential = Credentials.basic(
                        BuildConfig.OMISE_PUBLIC_KEY,
                        BuildConfig.OMISE_PASSWORD
                    )
                    val authenticatedRequest: Request = request.newBuilder()
                        .header("Authorization", credential).build()
                    return chain.proceed(authenticatedRequest)
                }

            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(OMISE_BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        retrofit.create(OmiseApi::class.java)
    }
}