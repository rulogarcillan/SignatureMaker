/*
 __ _                   _                                 _
/ _(_) __ _ _ __   __ _| |_ _   _ _ __ ___    /\/\   __ _| | _____ _ __
\ \| |/ _` | '_ \ / _` | __| | | | '__/ _ \  /    \ / _` | |/ / _ \ '__|
_\ \ | (_| | | | | (_| | |_| |_| | | |  __/ / /\/\ \ (_| |   <  __/ |
\__/_|\__, |_| |_|\__,_|\__|\__,_|_|  \___| \/    \/\__,_|_|\_\___|_|
      |___/

Copyright (C) 2018  Raúl Rodríguez Concepción www.tuppersoft.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/
package com.signaturemaker.app.application.core.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.signaturemaker.app.BuildConfig
import com.signaturemaker.app.application.features.menu.SettingFragment
import com.signaturemaker.app.application.utils.Constants
import com.signaturemaker.app.data.repositories.SharedPreferencesRepository
import com.signaturemaker.app.domain.models.ItemFile
import java.io.File
import java.text.DateFormat

object Utils {

    var sortOrder = Constants.DEFAULT_SORT_ORDER
    var maxStroke = Constants.DEFAULT_MAX_STROKE
    var minStroke = Constants.DEFAULT_MIN_TROKE
    var penColor = Constants.DEFAULT_PEN_COLOR
    var wallpaper = Constants.DEFAULT_WALLPAPER
    var path = Constants.DEFAULT_PATH
    var disableAds: Boolean = Constants.DEFAULT_DISABLE_ADS
    var nameSave: Boolean = Constants.DEFAULT_NAME_SAVE
    var deleteExit: Boolean = Constants.DEFAULT_DELETE_EXIT

    private fun defaultColor() {
        penColor = Constants.DEFAULT_PEN_COLOR
    }

    private fun defaultDeleteExit() {
        deleteExit = Constants.DEFAULT_DELETE_EXIT
    }

    private fun defaultDisableAds() {
        disableAds = Constants.DEFAULT_DISABLE_ADS
    }

    private fun defaultNameSave() {
        nameSave = Constants.DEFAULT_NAME_SAVE
    }

    fun defaultPath() {
        path = Constants.DEFAULT_PATH
    }

    private fun defaultStroke() {
        maxStroke = Constants.DEFAULT_MAX_STROKE
        minStroke = Constants.DEFAULT_MIN_TROKE
    }

    private fun defaultWallpaper() {
        wallpaper = Constants.DEFAULT_WALLPAPER
    }

    fun defaultValues() {
        nameSave = Constants.DEFAULT_NAME_SAVE
        disableAds = Constants.DEFAULT_DISABLE_ADS
        sortOrder = Constants.DEFAULT_SORT_ORDER
        maxStroke = Constants.DEFAULT_MAX_STROKE
        minStroke = Constants.DEFAULT_MIN_TROKE
        penColor = Constants.DEFAULT_PEN_COLOR
        wallpaper = Constants.DEFAULT_WALLPAPER
        path = Constants.DEFAULT_PATH
        deleteExit = Constants.DEFAULT_DELETE_EXIT
    }




    fun loadAllPreferences(mContext: Context?) {

        mContext?.let {
            if (SharedPreferencesRepository.loadPreference(mContext, Constants.ID_PREF_WALLPAPER, false)) {
                wallpaper = SharedPreferencesRepository.loadPreference(
                    mContext,
                    Constants.PREF_WALLPAPER,
                    Constants.DEFAULT_WALLPAPER
                )
            } else {
                defaultWallpaper()
            }
            if (SharedPreferencesRepository.loadPreference(mContext, Constants.ID_PREF_COLOR, false)) {
                penColor = SharedPreferencesRepository
                    .loadPreference(mContext, Constants.PREF_COLOR, Constants.DEFAULT_PEN_COLOR)
            } else {
                defaultColor()
            }
            if (SharedPreferencesRepository.loadPreference(mContext, Constants.ID_PREF_STROKE, false)) {
                minStroke = SharedPreferencesRepository
                    .loadPreference(mContext, Constants.PREF_MIN_TROKE, Constants.DEFAULT_MIN_TROKE)
                maxStroke = SharedPreferencesRepository
                    .loadPreference(mContext, Constants.PREF_MAX_TROKE, Constants.DEFAULT_MAX_STROKE)
            } else {
                defaultStroke()
            }
            if (SharedPreferencesRepository.loadPreference(mContext, Constants.ID_PREF_ADVERTISING, false)) {
                disableAds = true
            } else {
                defaultDisableAds()
            }
            if (SharedPreferencesRepository.loadPreference(mContext, Constants.ID_PREF_NAME, false)) {
                nameSave = true
            } else {
                defaultNameSave()
            }
            if (SharedPreferencesRepository.loadPreference(mContext, Constants.ID_PREF_DELETE, false)) {
                deleteExit = true
            } else {
                defaultDeleteExit()
            }
        }
    }

    fun saveAllPreferences(mContext: Context?) {
        mContext?.let {
            if (SharedPreferencesRepository.loadPreference(mContext, Constants.ID_PREF_COLOR, false)) {
                SharedPreferencesRepository.savePreference(mContext, Constants.PREF_COLOR, penColor)
            }
            if (SharedPreferencesRepository.loadPreference(mContext, Constants.ID_PREF_STROKE, false)) {
                SharedPreferencesRepository.savePreference(mContext, Constants.PREF_MIN_TROKE, minStroke)
                SharedPreferencesRepository.savePreference(mContext, Constants.PREF_MAX_TROKE, maxStroke)
            }
            if (SharedPreferencesRepository.loadPreference(mContext, Constants.ID_PREF_WALLPAPER, false)) {
                SharedPreferencesRepository.savePreference(mContext, Constants.PREF_WALLPAPER, wallpaper)
            }
        }
    }

    fun shareSign(mActivity: Activity, name: String) {
        val uri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID, File("$path/$name"))
        val imageUris: ArrayList<Uri> = arrayListOf(uri)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND_MULTIPLE
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)
            type = if (name.contains(".png") || name.contains(".PNG")) {
                "image/*"
            } else {
                "text/html"
            }
        }
        mActivity.startActivity(Intent.createChooser(shareIntent, "Share images to.."))
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


