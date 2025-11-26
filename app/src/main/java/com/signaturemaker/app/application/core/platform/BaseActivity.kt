package com.signaturemaker.app.application.core.platform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Base Activity to enable edge-to-edge support in all activities.
 */
abstract class BaseActivity : ComponentActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    /**
     * Override to prevent saving large Bitmaps to Bundle which causes
     * "Could not copy bitmap to parcel blob" crash.
     * Compose manages its own state restoration through rememberSaveable.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        // Don't call super to avoid automatic state saving that includes large Bitmaps
        // outState is left empty intentionally
        // Compose's rememberSaveable will handle necessary state restoration
    }
}
