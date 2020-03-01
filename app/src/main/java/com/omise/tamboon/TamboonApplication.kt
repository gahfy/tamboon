package com.omise.tamboon

import android.app.Application
import com.omise.tamboon.di.module.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

// Safe suppress as it is used in Manifest
@Suppress("unused")
class TamboonApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@TamboonApplication)
            modules(networkModule)
        }
    }
}