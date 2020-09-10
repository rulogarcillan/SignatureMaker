package com.signaturemaker.app.application.core.platform

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import com.google.android.material.snackbar.Snackbar
import com.signaturemaker.app.R

class PermissionRequester(
    val activity: ComponentActivity,
    private val permission: String,
    view: View? = null
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
                view?.let {
                    Snackbar.make(it, R.string.body_permissions, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.title_setting) {
                            goSetting()
                        }.show()
                }
                onDenied.invoke()
            }
        }

    fun runWithPermission(onGranted: () -> Unit, onDenied: () -> Unit) {
        this.onGranted = onGranted
        this.onDenied = onDenied
        requestPermissionLauncher.launch(permission)
    }

    fun goSetting() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + activity.packageName)
        )
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }
}

