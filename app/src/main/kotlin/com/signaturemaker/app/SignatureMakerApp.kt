package com.signaturemaker.app

import android.app.Application
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Environment
import com.google.firebase.analytics.FirebaseAnalytics
import com.signaturemaker.app.application.core.di.app.SignatureMakerDiImpl
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.application.utils.Constants.FOLDER_APP_NAME
import com.signaturemaker.app.application.core.analytics.Analytics
import com.tuppersoft.skizo.android.core.extension.logd
import java.io.File
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent

class SignatureMakerApp : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        "Init app".logd()

        // Initialize DI
        SignatureMakerDiImpl().start {
            androidContext(this@SignatureMakerApp)
        }

        // Initialize Analytics
        Analytics.init(FirebaseAnalytics.getInstance(this))

        setPath()
        Utils.loadAllPreferences(this)
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
}
