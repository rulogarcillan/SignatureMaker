package com.signaturemaker.app.Nucleo;

import android.util.Log;

import com.signaturemaker.app.BuildConfig;

public final class LogUtils {

    static final String TAG = "DEBUG_SIGNATURE_MAKER: ";

    public static void LOGD(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG + tag, message);
        }
    }

    public static void LOGV(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG + tag, message);
        }
    }

    public static void LOGI(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG + tag, message);
        }
    }

    public static void LOGW(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG + tag, message);
        }
    }

    public static void LOGE(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG + tag, message);
        }
    }

    public static void TRAZA(String message) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, message);

            String a = "adsasd";
        }
    }


}