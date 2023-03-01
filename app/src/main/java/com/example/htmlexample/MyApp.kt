package com.example.htmlexample

import android.app.Application
import android.os.StrictMode

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        StrictMode.enableDefaults()
    }
}