package com.signaturemaker.app.application.core.extensions

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by Raúl Rodríguez Concepción on 2020-01-13.

 * raulrcs@gmail.com
 */

@SuppressLint("SimpleDateFormat")
fun Long.toDateStringLocalFormat(): String {
    val date = Date(this)
    val df2 = SimpleDateFormat("dd/MM/yyyy")
    return df2.format(date)
}
