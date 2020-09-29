package com.signaturemaker.app.application

import android.app.Application
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Environment
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import com.signaturemaker.app.BuildConfig
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.application.utils.Constants.FOLDER_APP_NAME
import com.tuppersoft.skizo.android.core.extension.logd
import dagger.hilt.android.HiltAndroidApp
import java.io.File

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        "Init app".logd()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
        setPath()
        Utils.loadAllPreferences(this)
        loadTheme()
    }

    private fun setPath() {
        Utils.path = if (VERSION.SDK_INT >= VERSION_CODES.Q) {
            Environment.DIRECTORY_PICTURES + File.separator + FOLDER_APP_NAME + File.separator
        } else {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + FOLDER_APP_NAME + File.separator)
                .toString()
        }

        Utils.path.logd()
    }

    private fun loadTheme() {
        AppCompatDelegate.setDefaultNightMode(Utils.themeMode)
    }
}
