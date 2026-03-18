package com.signaturemaker.app.application.core.analytics

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Simple Analytics singleton object.
 * Usage: Analytics.trackSignatureSaved(...)
 */
object Analytics {

    private const val TAG = "Analytics_tag"
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    /**
     * Initialize Firebase Analytics instance
     * Call this from Application.onCreate()
     */
    fun init(analytics: FirebaseAnalytics) {
        firebaseAnalytics = analytics
    }

    // ============================================
    // SCREEN TRACKING
    // ============================================

    fun trackScreen(screenName: String) {
        Log.i(TAG, "Screen: $screenName")
        SessionTracker.startScreen(screenName)
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    // ============================================
    // SIGNATURE EVENTS
    // ============================================

    /**
     * Track when a signature is saved
     * Usa SessionTracker automáticamente para calcular métricas
     */
    fun trackSignatureSaved(
        isTransparent: Boolean,
        penColor: Long,
        strokeMin: Float,
        strokeMax: Float,
        backgroundType: Int
    ) {
        // Obtener datos de la sesión
        val secondsSinceLastSave = SessionTracker.getSecondsSinceLastSave()
        SessionTracker.incrementSignatureCount()
        val signaturesCount = SessionTracker.getSignaturesCount()
        SessionTracker.updateLastSaveTime()

        Log.i(TAG, "Signature saved: count=$signaturesCount, timeSince=${secondsSinceLastSave}s")

        val bundle = Bundle()
        bundle.putString("save_type", if (isTransparent) "transparent" else "white_background")
        bundle.putLong("signatures_count", signaturesCount.toLong())
        bundle.putLong("seconds_since_last_save", secondsSinceLastSave)
        bundle.putLong("pen_color", penColor)
        bundle.putDouble("stroke_width_min", strokeMin.toDouble())
        bundle.putDouble("stroke_width_max", strokeMax.toDouble())
        bundle.putLong("background_type", backgroundType.toLong())
        firebaseAnalytics.logEvent("signature_saved", bundle)
    }

    fun trackSignatureShared(isTransparent: Boolean) {
        Log.i(TAG, "Signature shared: ${if (isTransparent) "transparent" else "white"}")
        val bundle = Bundle()
        bundle.putString("share_type", if (isTransparent) "transparent" else "white_background")
        firebaseAnalytics.logEvent("signature_shared", bundle)
    }

    fun trackSignatureCleared() {
        Log.i(TAG, "Signature cleared")
        firebaseAnalytics.logEvent("signature_cleared", null)
    }

    fun trackSignatureSaveFailed() {
        Log.i(TAG, "Signature save failed")
        firebaseAnalytics.logEvent("signature_save_failed", null)
    }

    fun trackSignScreenSessionEnd() {
        val durationSeconds = SessionTracker.getCurrentScreenDuration()
        val signaturesSaved = SessionTracker.getSignaturesCount()

        Log.i(TAG, "Sign session end: ${durationSeconds}s, $signaturesSaved signatures")
        val bundle = Bundle()
        bundle.putLong("session_duration_seconds", durationSeconds)
        bundle.putLong("signatures_saved", signaturesSaved.toLong())
        firebaseAnalytics.logEvent("sign_screen_session_end", bundle)
    }

    // ============================================
    // UI INTERACTION EVENTS
    // ============================================

    fun trackPenColorChanged(colorValue: Long) {
        Log.i(TAG, "Pen color changed: $colorValue")
        val bundle = Bundle()
        bundle.putLong("color_value", colorValue)
        firebaseAnalytics.logEvent("pen_color_changed", bundle)
    }

    fun trackStrokeWidthChanged(minWidth: Float, maxWidth: Float) {
        Log.i(TAG, "Stroke width changed: $minWidth - $maxWidth")
        val bundle = Bundle()
        bundle.putDouble("min_width", minWidth.toDouble())
        bundle.putDouble("max_width", maxWidth.toDouble())
        firebaseAnalytics.logEvent("stroke_width_changed", bundle)
    }

    fun trackBackgroundChanged(backgroundType: Int) {
        Log.i(TAG, "Background changed: $backgroundType")
        val bundle = Bundle()
        bundle.putLong("background_type", backgroundType.toLong())
        firebaseAnalytics.logEvent("background_changed", bundle)
    }

    // ============================================
    // AD EVENTS
    // ============================================

    fun trackInterstitialShown() {
        val signaturesInSession = SessionTracker.getSignaturesCount()
        Log.i(TAG, "Interstitial shown after $signaturesInSession signatures")
        val bundle = Bundle()
        bundle.putLong("signatures_in_session", signaturesInSession.toLong())
        firebaseAnalytics.logEvent("interstitial_shown", bundle)
    }

    // ============================================
    // FILES EVENTS
    // ============================================

    fun trackFileOpened(fileType: String) {
        Log.i(TAG, "File opened: $fileType")
        val bundle = Bundle()
        bundle.putString("file_type", fileType)
        firebaseAnalytics.logEvent("file_opened", bundle)
    }

    fun trackFileDeleted(fileType: String) {
        Log.i(TAG, "File deleted: $fileType")
        val bundle = Bundle()
        bundle.putString("file_type", fileType)
        firebaseAnalytics.logEvent("file_deleted", bundle)
    }

    fun trackAllFilesDeleted(filesCount: Int) {
        Log.i(TAG, "All files deleted: $filesCount files")
        val bundle = Bundle()
        bundle.putLong("files_count", filesCount.toLong())
        firebaseAnalytics.logEvent("all_files_deleted", bundle)
    }

    // ============================================
    // SETTINGS EVENTS
    // ============================================

    fun trackSettingChanged(settingName: String, value: String) {
        Log.i(TAG, "Setting changed: $settingName = $value")
        val bundle = Bundle()
        bundle.putString("setting_name", settingName)
        bundle.putString("value", value)
        firebaseAnalytics.logEvent("setting_changed", bundle)
    }

    fun trackDefaultsRestored() {
        Log.i(TAG, "Defaults restored")
        firebaseAnalytics.logEvent("defaults_restored", null)
    }
}

