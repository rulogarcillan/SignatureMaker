package com.tuppersoft.skizo.android.core.logger

import android.util.Log

/**
 * Created by Raúl Rodríguez Concepción on 29/07/2020.
 * raulrcs@gmail.com
 */

object SkizoLog {

    private var enabled = true
    const val TAG = "SkizoLogger"

    fun enabledLogger(flag: Boolean) {
        enabled = flag
    }

    fun v(msg: String = "", tr: Throwable? = null) {
        v(buildTag(), msg, tr)
    }

    fun v(tag: String, msg: String = "", tr: Throwable? = null) {
        if (enabled) {
            if (tr == null) {
                Log.v(tag, msg, tr)
            } else {
                Log.v(tag, msg)
            }
        }
    }

    fun d(msg: String = "", tr: Throwable? = null) {
        d(buildTag(), msg, tr)
    }

    fun d(tag: String, msg: String = "", tr: Throwable? = null) {
        if (enabled) {
            if (tr == null) {
                Log.d(tag, msg, tr)
            } else {
                Log.d(tag, msg)
            }
        }
    }

    fun i(msg: String = "", tr: Throwable? = null) {
        i(buildTag(), msg, tr)
    }

    fun i(tag: String, msg: String = "", tr: Throwable? = null) {
        if (enabled) {
            if (tr == null) {
                Log.i(tag, msg, tr)
            } else {
                Log.i(tag, msg)
            }
        }
    }

    fun w(msg: String = "", tr: Throwable? = null) {
        w(buildTag(), msg, tr)
    }

    fun w(tag: String, msg: String = "", tr: Throwable? = null) {
        if (enabled) {
            if (tr == null) {
                Log.w(tag, msg, tr)
            } else {
                Log.w(tag, msg)
            }
        }
    }

    fun e(msg: String = "", tr: Throwable? = null) {
        e(buildTag(), msg, tr)
    }

    fun e(tag: String, msg: String = "", tr: Throwable? = null) {
        if (enabled) {
            if (tr == null) {
                Log.e(tag, msg, tr)
            } else {
                Log.e(tag, msg)
            }
        }
    }

    private fun buildTag(): String {
        return try {
            "$TAG " + Thread.currentThread().stackTrace[5].fileName.split("\\.".toRegex())
                .toTypedArray()[0] + "." + Thread.currentThread().stackTrace[5]
                .methodName + ":" + Thread.currentThread().stackTrace[5].lineNumber
        } catch (_: Throwable) {
            TAG
        }
    }
}

