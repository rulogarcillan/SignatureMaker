package com.signaturemaker.app.application.core.extensions

import android.app.Activity
import com.google.android.material.snackbar.Snackbar

/**
 * Show Snackbar
 */
fun Activity.createSnackBar(
    msg: String,
    actionMsg: String? = null,
    callback: Snackbar.Callback? = null
): Snackbar {
    return Snackbar.make(this.findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).apply {
        actionMsg?.let {
            setAction(actionMsg) {}
        }
        callback?.let {
            addCallback(callback)
        }
    }
}

