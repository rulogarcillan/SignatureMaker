package com.signaturemaker.app.application.core.extensions

import android.content.Context
import android.widget.Toast

/**
 * Show toats
 */
fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}
