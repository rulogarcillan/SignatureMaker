package com.signaturemaker.app.application.core.extensions

import android.Manifest.permission
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.signaturemaker.app.application.features.menu.SettingFragment

/**
 * Show toats
 */
fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

/**
 * Star Activity to rate it
 */
fun Context.openRate() {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(SettingFragment.urlRate)
        this.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
    }
}

fun Context.hasPermissionWriteRead(): Boolean {
    return ActivityCompat.checkSelfPermission(
        this,
        permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
        this,
        permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED
}

