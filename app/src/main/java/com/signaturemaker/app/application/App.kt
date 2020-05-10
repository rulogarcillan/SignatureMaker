package com.signaturemaker.app.application

import android.app.Application
import android.util.Log
import com.facebook.stetho.BuildConfig
import com.facebook.stetho.Stetho
//import com.gu.toolargetool.TooLargeTool
import com.signaturemaker.app.application.utils.Constants.TAG

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Iniciamos la app")
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            //  TooLargeTool.startLogging(this)
        }
    }
}