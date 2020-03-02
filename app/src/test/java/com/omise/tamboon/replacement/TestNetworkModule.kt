package com.omise.tamboon.replacement

import org.koin.dsl.module

val testNetworkModule = module {
    single<com.omise.tamboon.network.TamboonApi> {
        TamboonApi()
    }

    single<com.omise.tamboon.network.OmiseApi> {
        OmiseApi()
    }
}