package com.signaturemaker.app.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.stetho.BuildConfig
import com.facebook.stetho.Stetho
import com.signaturemaker.app.data.repositories.SharedPreferencesRepository
import com.tuppersoft.skizo.core.extension.logd
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        "Init app".logd()
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
