package com.tuppersoft.skizo.android.core.extension

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.preference.PreferenceManager

/**
 * Get color from attr
 */
@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

/**
 * Guarda cualquier valor primitivo en el shared preferences
 */
inline fun <reified T> Context.saveSharedPreference(key: String, value: T) {

    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    val editor = sharedPreferences.edit()

    when (value) {
        is Boolean -> editor.putBoolean(key, value)
        is Float -> editor.putFloat(key, value)
        is Int -> editor.putInt(key, value)
        is Long -> editor.putLong(key, value)
        is String -> editor.putString(key, value)
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
    editor.apply()
}

/**
 * Carga cualquier valor primitivo del shared preferences
 */
inline fun <reified T : Any> Context.loadSharedPreference(key: String, defaultValue: T): T {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    return when (defaultValue) {
        is Boolean -> sharedPreferences.getBoolean(key, defaultValue) as T
        is Float -> sharedPreferences.getFloat(key, defaultValue) as T
        is Int -> sharedPreferences.getInt(key, defaultValue) as T
        is Long -> sharedPreferences.getLong(key, defaultValue) as T
        is String -> sharedPreferences.getString(key, defaultValue) as T
        else -> defaultValue
    }
}

/**
 * Elimina la shared preference para la key dada
 */
fun Context.removeSharedPreference(key: String) {
    val mySPrefs = PreferenceManager.getDefaultSharedPreferences(this)
    val editor = mySPrefs.edit()
    if (mySPrefs.contains(key)) {
        editor.remove(key)
        editor.apply()
    }
}
