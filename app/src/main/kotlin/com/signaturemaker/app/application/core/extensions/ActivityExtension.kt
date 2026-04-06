package com.signaturemaker.app.application.core.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri

private const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.signaturemaker.app"

fun Activity.shareSign(uri: Uri) {
    val imageUris: ArrayList<Uri> = arrayListOf(uri)
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND_MULTIPLE
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)
        putExtra(Intent.EXTRA_TEXT, "Created with Signature Maker\n$PLAY_STORE_URL")
        type =
            if (uri.path?.contains(".png") == true || uri.path?.contains(".PNG") == true || uri.path?.contains("images") == true) {
                "image/*"
            } else {
                "text/html"
            }
    }
    this.startActivity(Intent.createChooser(shareIntent, "Share images to..."))
}
