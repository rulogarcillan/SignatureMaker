package com.signaturemaker.app.application

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.stetho.BuildConfig
import com.facebook.stetho.Stetho
//import com.gu.toolargetool.TooLargeTool
import com.signaturemaker.app.application.utils.Constants.TAG
import com.signaturemaker.app.data.repositories.SharedPreferencesRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Iniciamos la app")
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        AppCompatDelegate.setDefaultNightMode(
            SharedPreferencesRepository.loadPreference(
                this,
                "THEME_MODE",
                AppCompatDelegate.MODE_NIGHT_NO
            )
        )
    }
}
