package com.practice.codingtestapp.core

import android.app.Application
import android.content.Context
import com.practice.codingtestapp.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CodingTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        lateinit  var appContext: Context
    }
}