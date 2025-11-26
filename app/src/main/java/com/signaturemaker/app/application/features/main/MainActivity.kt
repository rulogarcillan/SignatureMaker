package com.signaturemaker.app.application.features.main

import android.os.Bundle
import androidx.activity.compose.setContent
import com.signaturemaker.app.application.core.platform.BaseActivity

/*
 * Main Activity - Entry point for the application
 * All UI logic is delegated to MainRoute and MainScreen composables
 */
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainRoute()
        }
    }
}
