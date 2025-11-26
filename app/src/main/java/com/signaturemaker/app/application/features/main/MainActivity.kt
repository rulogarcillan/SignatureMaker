package com.signaturemaker.app.application.features.main

import android.os.Bundle
import androidx.activity.compose.setContent
import com.signaturemaker.app.application.core.platform.BaseActivity
import com.signaturemaker.app.application.ui.navigation.SignatureMakerApp

/*
 * Main Activity - Entry point for the application
 * Sets up the main screen with navigation
 */
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignatureMakerApp()
        }
    }
}
