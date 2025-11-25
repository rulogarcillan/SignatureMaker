
package com.signaturemaker.app.utils

import android.graphics.Color
import android.os.Environment
import androidx.appcompat.app.AppCompatDelegate

object Constants {

    const val ID_THEME_MODE = "id_theme_mode"
    const val ID_PREF_ADVERTISING = "id_pref_advertising"
    const val ID_PREF_PATH = "id_pref_path"
    const val ID_PREF_DELETE = "id_pref_delete"
    const val ID_PREF_NAME = "id_pref_name"
    const val ID_PREF_COLOR = "id_pref_color"
    const val ID_PREF_STROKE = "id_pref_stroke"
    const val ID_PREF_RESET = "id_pref_reset"
    const val ID_PREF_WALLPAPER = "id_pref_wallpaper"
    const val PREF_MAX_TROKE = "MAX_STROKE"
    const val PREF_MIN_TROKE = "MIN_STROKE"
    const val PREF_COLOR = "COLOR"
    const val PREF_WALLPAPER = "WALLPAPER"
    const val FIRST_TIME = "first_time"
    const val NEED_MIGRATE = "need_migrate"
    const val DEFAULT_NAME_SAVE = false
    const val DEFAULT_DELETE_EXIT = false
    const val DEFAULT_DISABLE_ADS = false
    const val DEFAULT_SORT_ORDER = -1
    const val DEFAULT_THEME_MODE = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    const val DEFAULT_MAX_STROKE = 4
    const val DEFAULT_MIN_STROKE = 1
    const val DEFAULT_WALLPAPER = 3
    val DEFAULT_PEN_COLOR = Color.parseColor("#200BB2")
    private val ROOT = Environment.getExternalStorageDirectory().absolutePath
    val DEFAULT_OLD_PATH = "$ROOT/Signature/"
    const val FOLDER_APP_NAME = "Signature maker"
}
