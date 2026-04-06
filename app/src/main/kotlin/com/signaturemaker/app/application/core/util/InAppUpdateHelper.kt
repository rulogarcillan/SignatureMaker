package com.signaturemaker.app.application.core.util

import android.app.Activity
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.signaturemaker.app.BuildConfig
import com.tuppersoft.skizo.android.core.extension.logd
import com.tuppersoft.skizo.android.core.extension.loge

/**
 * Helper for Google Play In-App Update API.
 * Uses FLEXIBLE updates: downloads in background, prompts user to restart when ready.
 *
 * Note: Only works on release builds installed from Play Store.
 */
object InAppUpdateHelper {

    private var installStateUpdatedListener: InstallStateUpdatedListener? = null

    /**
     * Check for updates and start a flexible update if available.
     * @param activity The activity to use for the update flow
     * @param onUpdateDownloaded Callback when update is downloaded and ready to install
     */
    fun checkForUpdate(activity: Activity, onUpdateDownloaded: () -> Unit = {}) {
        if (BuildConfig.DEBUG) {
            "InAppUpdate: Skipped - debug build".logd()
            return
        }

        try {
            val appUpdateManager = AppUpdateManagerFactory.create(activity)
            val appUpdateInfoTask = appUpdateManager.appUpdateInfo

            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                "InAppUpdate: Update availability = ${appUpdateInfo.updateAvailability()}".logd()

                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
                ) {
                    "InAppUpdate: Flexible update available, starting...".logd()

                    // Listen for download completion
                    installStateUpdatedListener = InstallStateUpdatedListener { state ->
                        if (state.installStatus() == InstallStatus.DOWNLOADED) {
                            "InAppUpdate: Update downloaded, ready to install".logd()
                            onUpdateDownloaded()
                        }
                    }
                    installStateUpdatedListener?.let {
                        appUpdateManager.registerListener(it)
                    }

                    appUpdateManager.startUpdateFlow(
                        appUpdateInfo,
                        activity,
                        AppUpdateOptions.defaultOptions(AppUpdateType.FLEXIBLE)
                    )
                } else {
                    "InAppUpdate: No update available or not allowed".logd()
                }
            }

            appUpdateInfoTask.addOnFailureListener { exception ->
                "InAppUpdate: Failed to check for updates: ${exception.message}".loge()
            }
        } catch (e: Exception) {
            "InAppUpdate: Error: ${e.message}".loge()
        }
    }

    /**
     * Complete the update (restart the app with the new version).
     * Call this when the user accepts the update after download.
     */
    fun completeUpdate(activity: Activity) {
        try {
            val appUpdateManager = AppUpdateManagerFactory.create(activity)
            appUpdateManager.completeUpdate()
            installStateUpdatedListener?.let {
                appUpdateManager.unregisterListener(it)
            }
            installStateUpdatedListener = null
        } catch (e: Exception) {
            "InAppUpdate: Error completing update: ${e.message}".loge()
        }
    }
}
