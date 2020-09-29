package com.signaturemaker.app.application.core.extensions

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Raúl Rodríguez Concepción on 01/09/2020.
 * raulrcs@gmail.com
 */

fun String.isValidEmail(): Boolean {
    val pattern: Pattern = Pattern.compile(".+@.+\\.[a-z]+")
    val matcher: Matcher = pattern.matcher(this)
    return matcher.matches()
}
