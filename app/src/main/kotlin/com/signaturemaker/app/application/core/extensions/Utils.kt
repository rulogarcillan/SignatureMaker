package com.signaturemaker.app.application.core.extensions

import android.content.Context
import com.signaturemaker.app.application.utils.Constants
import com.signaturemaker.app.domain.models.ItemFile
import com.tuppersoft.skizo.android.core.extension.loadSharedPreference
import com.tuppersoft.skizo.android.core.extension.saveSharedPreference

object Utils {

    var sortOrder = Constants.DEFAULT_SORT_ORDER
    var maxStroke = Constants.DEFAULT_MAX_STROKE
    var minStroke = Constants.DEFAULT_MIN_STROKE
    var penColor = Constants.DEFAULT_PEN_COLOR
    var wallpaper = Constants.DEFAULT_WALLPAPER
    var path = "" // load from application
    var deleteExit: Boolean = Constants.DEFAULT_DELETE_EXIT

    private fun defaultColor() {
        penColor = Constants.DEFAULT_PEN_COLOR
    }

    private fun defaultStroke() {
        maxStroke = Constants.DEFAULT_MAX_STROKE
        minStroke = Constants.DEFAULT_MIN_STROKE
    }

    private fun defaultWallpaper() {
        wallpaper = Constants.DEFAULT_WALLPAPER
    }

    fun defaultValues() {
        sortOrder = Constants.DEFAULT_SORT_ORDER
        maxStroke = Constants.DEFAULT_MAX_STROKE
        minStroke = Constants.DEFAULT_MIN_STROKE
        penColor = Constants.DEFAULT_PEN_COLOR
        wallpaper = Constants.DEFAULT_WALLPAPER
        deleteExit = Constants.DEFAULT_DELETE_EXIT
    }

    fun loadAllPreferences(mContext: Context?) {
        mContext?.let {
            if (mContext.loadSharedPreference(Constants.ID_PREF_WALLPAPER, false)) {
                wallpaper = mContext.loadSharedPreference(Constants.PREF_WALLPAPER, Constants.DEFAULT_WALLPAPER)
            } else {
                defaultWallpaper()
            }
            if (mContext.loadSharedPreference(Constants.ID_PREF_COLOR, false)) {
                penColor = mContext.loadSharedPreference(Constants.PREF_COLOR, Constants.DEFAULT_PEN_COLOR)
            } else {
                defaultColor()
            }
            if (mContext.loadSharedPreference(Constants.ID_PREF_STROKE, false)) {
                minStroke = mContext.loadSharedPreference(Constants.PREF_MIN_TROKE, Constants.DEFAULT_MIN_STROKE)
                maxStroke = mContext.loadSharedPreference(Constants.PREF_MAX_TROKE, Constants.DEFAULT_MAX_STROKE)
            } else {
                defaultStroke()
            }

            deleteExit = mContext.loadSharedPreference(Constants.ID_PREF_DELETE, Constants.DEFAULT_DELETE_EXIT)
        }
    }

    fun saveAllPreferences(mContext: Context?) {
        mContext?.let {
            if (mContext.loadSharedPreference(Constants.ID_PREF_COLOR, false)) {
                mContext.saveSharedPreference(Constants.PREF_COLOR, penColor)
            }
            if (mContext.loadSharedPreference(Constants.ID_PREF_STROKE, false)) {
                mContext.saveSharedPreference(Constants.PREF_MIN_TROKE, minStroke)
                mContext.saveSharedPreference(Constants.PREF_MAX_TROKE, maxStroke)
            }
            if (mContext.loadSharedPreference(Constants.ID_PREF_WALLPAPER, false)) {
                mContext.saveSharedPreference(Constants.PREF_WALLPAPER, wallpaper)
            }
        }
    }

    fun sort(list: List<ItemFile>, type: Int): List<ItemFile> {
        val temporalList = list.toMutableList()
        if (type == 1) {
            temporalList.sortByDescending { it.date }
        } else {
            temporalList.sortBy { it.date }
        }

        return temporalList.toList()
    }
}
