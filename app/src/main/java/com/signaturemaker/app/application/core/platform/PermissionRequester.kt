package com.signaturemaker.app.application.core.platform

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission

class PermissionRequester(
    activity: ComponentActivity,
    private val permission: String
) {

    private var onGranted: () -> Unit = {}
    private var onDenied: () -> Unit = {}

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        activity.registerForActivityResult(
            RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                onGranted.invoke()
            } else {
                onDenied.invoke()
            }
        }

    fun runWithPermission(onGranted: () -> Unit, onDenied: () -> Unit) {
        this.onGranted = onGranted
        this.onDenied = onDenied
        requestPermissionLauncher.launch(permission)
    }
}
