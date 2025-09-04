package com.example.wildsafari

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Firebase is auto-initialized, no need for manual setup here
        // You can add other app-wide initialization code if needed
    }
}