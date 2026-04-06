package com.signaturemaker.app.application.core.util

import android.app.Activity
import android.content.Context
import com.google.android.play.core.review.ReviewManagerFactory
import com.signaturemaker.app.BuildConfig
import com.tuppersoft.skizo.android.core.extension.logd
import com.tuppersoft.skizo.android.core.extension.loge

/**
 * Helper for Google Play In-App Review API.
 * Prompts users to rate the app without leaving it.
 *
 * Strategy: Show after the user has saved 3+ signatures in total (across sessions),
 * max once every 30 days. Google also has its own internal quota that limits frequency.
 *
 * Note: In-App Review only works on release builds installed from Play Store.
 * In debug builds, the review flow is skipped to avoid crashes.
 */
object InAppReviewHelper {

    private const val PREFS_NAME = "in_app_review"
    private const val KEY_LAST_REVIEW_PROMPT = "last_review_prompt"
    private const val KEY_TOTAL_SAVES = "total_saves"
    private const val MIN_SAVES_BEFORE_PROMPT = 3
    private const val MIN_DAYS_BETWEEN_PROMPTS = 30L
    private const val MILLIS_PER_DAY = 86_400_000L

    /**
     * Track a signature save. Call this every time the user saves a signature.
     */
    fun trackSignatureSaved(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val totalSaves = prefs.getInt(KEY_TOTAL_SAVES, 0) + 1
        prefs.edit().putInt(KEY_TOTAL_SAVES, totalSaves).apply()
        "InAppReview: Total saves = $totalSaves".logd()
    }

    /**
     * Try to show the In-App Review dialog if conditions are met.
     * Conditions:
     * - Not a debug build (In-App Review crashes on debug)
     * - User has saved at least MIN_SAVES_BEFORE_PROMPT signatures
     * - At least MIN_DAYS_BETWEEN_PROMPTS days since last prompt
     * - Google's own internal quota is not exceeded
     */
    fun requestReviewIfEligible(activity: Activity) {
        if (BuildConfig.DEBUG) {
            "InAppReview: Skipped - debug build".logd()
            return
        }

        try {
            val prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val totalSaves = prefs.getInt(KEY_TOTAL_SAVES, 0)
            val lastPrompt = prefs.getLong(KEY_LAST_REVIEW_PROMPT, 0L)
            val daysSinceLastPrompt = (System.currentTimeMillis() - lastPrompt) / MILLIS_PER_DAY

            if (totalSaves < MIN_SAVES_BEFORE_PROMPT) {
                "InAppReview: Not eligible - only $totalSaves saves (need $MIN_SAVES_BEFORE_PROMPT)".logd()
                return
            }

            if (lastPrompt > 0 && daysSinceLastPrompt < MIN_DAYS_BETWEEN_PROMPTS) {
                "InAppReview: Not eligible - only $daysSinceLastPrompt days since last prompt".logd()
                return
            }

            "InAppReview: Eligible! Requesting review flow...".logd()

            val reviewManager = ReviewManagerFactory.create(activity)
            val request = reviewManager.requestReviewFlow()

            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reviewInfo = task.result
                    val flow = reviewManager.launchReviewFlow(activity, reviewInfo)
                    flow.addOnCompleteListener {
                        prefs.edit().putLong(KEY_LAST_REVIEW_PROMPT, System.currentTimeMillis()).apply()
                        "InAppReview: Review flow completed".logd()
                    }
                } else {
                    "InAppReview: Failed to request review flow: ${task.exception?.message}".loge()
                }
            }
        } catch (e: Exception) {
            "InAppReview: Error: ${e.message}".loge()
        }
    }
}
