package com.signaturemaker.app.application.core.util

import android.content.Context

/**
 * Centralized feature flags for the app.
 * Code-toggleable: flip the const to enable/disable features without recompiling logic.
 */
object FeatureFlags {

    /**
     * Master toggle for the onboarding flow.
     * Set to `true` to show onboarding to first-time users.
     * Set to `false` to completely disable it (skip for everyone).
     */
    const val ONBOARDING_ENABLED = true

    private const val PREFS_NAME = "feature_flags"
    private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"

    /**
     * Returns true if onboarding should be shown:
     * - Feature flag is enabled AND
     * - User has not completed onboarding yet
     */
    fun shouldShowOnboarding(context: Context): Boolean {
        if (!ONBOARDING_ENABLED) return false
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return !prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    /**
     * Mark onboarding as completed so it won't show again.
     */
    fun completeOnboarding(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, true).apply()
    }

    /**
     * Reset onboarding state (for testing purposes).
     */
    fun resetOnboarding(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, false).apply()
    }
}
