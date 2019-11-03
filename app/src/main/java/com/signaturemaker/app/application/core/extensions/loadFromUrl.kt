package com.signaturemaker.app.application.core.extensions

import android.widget.ImageView
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.platform.GlideApp

fun ImageView.loadFromUrl(url: String) {
    when {
        url.toLowerCase().endsWith("png") -> {
            GlideApp.with(context).load(url).placeholder(R.drawable.ic_svg_icon).into(this)
        }
        url.toLowerCase().endsWith("svg") -> {
            GlideApp.with(context).load(url).placeholder(R.drawable.ic_svg_icon).into(this)
        }
    }
}