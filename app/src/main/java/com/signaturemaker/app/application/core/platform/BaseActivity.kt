package com.signaturemaker.app.application.core.platform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * Base Activity to enable edge-to-edge support in all activities.
 */
abstract class BaseActivity : ComponentActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var crashlytics: FirebaseCrashlytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        crashlytics = FirebaseCrashlytics.getInstance()

        // Setup Crashlytics user identification for better tracking
        crashlytics.setUserId("user_${System.currentTimeMillis()}")
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

    // ============================================
    // FIREBASE CRASHLYTICS TESTING FUNCTIONS
    // Call these from your UI to test Crashlytics
    // ============================================

    /**
     * Test 1: Force a simple crash (RuntimeException)
     * This will immediately crash the app and send report to Firebase Console
     *
     * Usage: Call from a button click or menu action
     * Result: App crashes, report appears in Firebase Console after ~5 minutes
     */
    protected fun testCrashSimple() {
        crashlytics.log("TEST: Forcing a simple crash")
        throw RuntimeException("Test Crash: This is a forced crash for testing Crashlytics")
    }

    /**
     * Test 2: Force a crash with custom key-value data
     * Useful to test if custom data is being captured
     *
     * Result: Crash report will include custom keys in Firebase Console
     */
    protected fun testCrashWithCustomData() {
        // Add custom keys that will appear in the crash report
        crashlytics.setCustomKey("test_type", "custom_data_crash")
        crashlytics.setCustomKey("screen_name", this::class.java.simpleName)
        crashlytics.setCustomKey("timestamp", System.currentTimeMillis())
        crashlytics.setCustomKey("test_string", "Testing custom string value")
        crashlytics.setCustomKey("test_number", 42)
        crashlytics.setCustomKey("test_boolean", true)

        crashlytics.log("TEST: Crash with custom data - Screen: ${this::class.java.simpleName}")

        throw RuntimeException("Test Crash: Crash with custom key-value data")
    }

    /**
     * Test 3: Log a non-fatal exception (won't crash the app)
     * This is useful to track errors that are caught and handled
     *
     * Result: Exception appears in Firebase Console without crashing
     */
    protected fun testNonFatalException() {
        crashlytics.log("TEST: Recording a non-fatal exception")

        // Create a custom exception with stack trace
        val exception = Exception("Test Non-Fatal: This error was caught and logged")

        // Add context
        crashlytics.setCustomKey("error_type", "non_fatal")
        crashlytics.setCustomKey("handled", true)

        // Record the exception without crashing
        crashlytics.recordException(exception)

        // App continues running
        android.util.Log.d("Crashlytics", "Non-fatal exception logged successfully")
    }

    /**
     * Test 4: Simulate a NullPointerException
     * Common type of crash to verify stack traces are captured correctly
     */
    protected fun testNullPointerCrash() {
        crashlytics.log("TEST: Simulating NullPointerException")
        crashlytics.setCustomKey("crash_type", "null_pointer")

        val nullString: String? = null
        // This will throw NullPointerException
        @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
        nullString!!.length
    }

    /**
     * Test 5: Test crash with breadcrumbs (logs)
     * Shows the sequence of events leading to the crash
     */
    protected fun testCrashWithBreadcrumbs() {
        crashlytics.log("Step 1: User opened the app")
        crashlytics.log("Step 2: User navigated to test screen")
        crashlytics.log("Step 3: User clicked test crash button")

        crashlytics.setCustomKey("breadcrumbs_test", true)
        crashlytics.setCustomKey("steps_before_crash", 3)

        crashlytics.log("Step 4: Triggering crash...")
        throw RuntimeException("Test Crash: Crash with breadcrumbs/logs")
    }

    /**
     * Test 6: Simulate an IndexOutOfBoundsException
     * Another common crash type
     */
    protected fun testIndexOutOfBoundsCrash() {
        crashlytics.log("TEST: Simulating IndexOutOfBoundsException")

        val list = listOf(1, 2, 3)
        // This will throw IndexOutOfBoundsException
        list[10]
    }
}
