package com.example.phonecontact

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PhoneContactApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}