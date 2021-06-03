package com.tuppersoft.skizo.android.core.extension

import com.tuppersoft.skizo.android.core.logger.SkizoLog

/**
 * Created by Raúl Rodríguez Concepción on 30/07/2020.
 * raulrcs@gmail.com
 */

fun String.logd(tr: Throwable? = null) {
    SkizoLog.d(buildTag(), this, tr)
}

fun String.logi(tr: Throwable? = null) {
    SkizoLog.i(buildTag(), this, tr)
}

fun String.logv(tr: Throwable? = null) {
    SkizoLog.v(buildTag(), this, tr)
}

fun String.logw(tr: Throwable? = null) {
    SkizoLog.w(buildTag(), this, tr)
}

fun String.loge(tr: Throwable? = null) {
    SkizoLog.e(buildTag(), this, tr)
}

private fun buildTag(): String {
    return try {
        "${SkizoLog.TAG} " + Thread.currentThread().stackTrace[5].fileName.split("\\.".toRegex())
            .toTypedArray()[0] + "." + Thread.currentThread().stackTrace[5]
            .methodName + ":" + Thread.currentThread().stackTrace[5].lineNumber
    } catch (_: Throwable) {
        SkizoLog.TAG
    }
}

