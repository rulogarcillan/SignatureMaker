package com.signaturemaker.app.application.core.extensions

import android.widget.ImageView
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.platform.GlideApp
import java.util.Locale

fun ImageView.loadFromUrl(url: String) {
    when {
        url.toLowerCase(Locale.ROOT).endsWith("png") -> {
            GlideApp.with(context).load(url).placeholder(R.drawable.ic_svg_icon).into(this)
        }
        url.toLowerCase(Locale.ROOT).endsWith("svg") -> {
            GlideApp.with(context).load(url).placeholder(R.drawable.ic_svg_icon).into(this)
        }
    }
}