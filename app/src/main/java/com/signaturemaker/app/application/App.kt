package com.signaturemaker.app.application

import android.app.Application
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import com.signaturemaker.app.BuildConfig
import com.signaturemaker.app.application.core.extensions.Utils
import com.tuppersoft.skizo.core.extension.logd
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
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI.path?.logd()
        setPath()
        Utils.loadAllPreferences(this)
        loadTheme()
    }

    private fun setPath() {
        //Utils.path = applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path + "/"
        Utils.path = if (VERSION.SDK_INT >= VERSION_CODES.Q) {
            Environment.DIRECTORY_PICTURES + File.separator + "Signature maker" + File.separator
        } else {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "Signature maker" + File.separator )
                .toString()
        }
    }

    private fun loadTheme() {
        AppCompatDelegate.setDefaultNightMode(Utils.themeMode)
    }
}
