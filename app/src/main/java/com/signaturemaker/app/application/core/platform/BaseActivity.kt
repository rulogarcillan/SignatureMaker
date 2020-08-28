package com.signaturemaker.app.application.core.platform

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.analytics.FirebaseAnalytics
import com.signaturemaker.app.data.repositories.SharedPreferencesRepository

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        AppCompatDelegate.setDefaultNightMode(
            SharedPreferencesRepository.loadPreference(
                this,
                "THEME_MODE",
                AppCompatDelegate.MODE_NIGHT_NO
            )
        )
    }
}
