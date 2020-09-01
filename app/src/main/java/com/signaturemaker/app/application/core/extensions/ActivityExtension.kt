package com.signaturemaker.app.application.core.extensions

import android.app.Activity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.signaturemaker.app.R

/**
 * Show Snackbar
 */
fun Activity.createSnackBar(
    msg: String, actionMsg: String,
    callback: Snackbar.Callback? = null
): Snackbar {
    return Snackbar.make(this.findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).apply {
        setAction(actionMsg) {}
        setActionTextColor(ContextCompat.getColor(this.context, R.color.secondaryColor))
        callback?.let {
            addCallback(callback)
        }
    }
}