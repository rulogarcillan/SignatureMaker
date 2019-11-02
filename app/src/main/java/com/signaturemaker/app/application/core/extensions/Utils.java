/*
 __ _                   _                                 _
/ _(_) __ _ _ __   __ _| |_ _   _ _ __ ___    /\/\   __ _| | _____ _ __
\ \| |/ _` | '_ \ / _` | __| | | | '__/ _ \  /    \ / _` | |/ / _ \ '__|
_\ \ | (_| | | | | (_| | |_| |_| | | |  __/ / /\/\ \ (_| |   <  __/ |
\__/_|\__, |_| |_|\__,_|\__|\__,_|_|  \___| \/    \/\__,_|_|\_\___|_|
      |___/

Copyright (C) 2018  Raúl Rodríguez Concepción www.wepica.com

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
package com.signaturemaker.app.application.core.extensions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.content.FileProvider;
import com.signaturemaker.app.BuildConfig;
import com.signaturemaker.app.application.utils.Constants;
import com.signaturemaker.app.data.repositories.SharedPreferencesRepository;
import com.signaturemaker.app.domain.models.ItemFile;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public final class Utils {

    public static int sortOrder = Constants.DEFAULT_SORT_ORDER;

    public static int maxStroke = Constants.DEFAULT_MAX_STROKE;

    public static int minStroke = Constants.DEFAULT_MIN_TROKE;

    public static int penColor = Constants.DEFAULT_PEN_COLOR;

    public static int wallpaper = Constants.DEFAULT_WALLPAPER;

    public static String path = Constants.DEFAULT_PATH;

    public static Boolean disableAds = Constants.DEFAULT_DISABLE_ADS;

    public static Boolean nameSave = Constants.DEFAULT_NAME_SAVE;

    public static Boolean deleteExit = Constants.DEFAULT_DELETE_EXIT;


    public static void defaultColor() {
        penColor = Constants.DEFAULT_PEN_COLOR;
    }

    public static void defaultDeleteExit() {
        deleteExit = Constants.DEFAULT_DELETE_EXIT;
    }

    public static void defaultDisableAds() {
        disableAds = Constants.DEFAULT_DISABLE_ADS;
    }

    public static void defaultNameSave() {
        nameSave = Constants.DEFAULT_NAME_SAVE;
    }

    public static void defaultPath() {
        path = Constants.DEFAULT_PATH;
    }

    public static void defaultStroke() {
        maxStroke = Constants.DEFAULT_MAX_STROKE;
        minStroke = Constants.DEFAULT_MIN_TROKE;
    }

    public static void defaultValues() {
        nameSave = Constants.DEFAULT_NAME_SAVE;
        disableAds = Constants.DEFAULT_DISABLE_ADS;
        sortOrder = Constants.DEFAULT_SORT_ORDER;
        maxStroke = Constants.DEFAULT_MAX_STROKE;
        minStroke = Constants.DEFAULT_MIN_TROKE;
        penColor = Constants.DEFAULT_PEN_COLOR;
        wallpaper = Constants.DEFAULT_WALLPAPER;
        path = Constants.DEFAULT_PATH;
        deleteExit = Constants.DEFAULT_DELETE_EXIT;
    }

    public static void defaultWallpaper() {
        wallpaper = Constants.DEFAULT_WALLPAPER;
    }

    /**
     * Get last time compilation app
     */
    public static String getAppTimeStamp(Context context) {
        String timeStamp = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            String appFile = appInfo.sourceDir;
            long time = new File(appFile).lastModified();
            DateFormat formatter = DateFormat.getDateTimeInstance();
            timeStamp = formatter.format(time);
        } catch (Exception e) {
            Log.e(Constants.TAG, e.getMessage());
        }
        return timeStamp;
    }

    public static void loadAllPreferences(Context mContext) {

        if (SharedPreferencesRepository.INSTANCE.loadPreference(mContext, Constants.ID_PREF_WALLPAPER, false)) {
            Utils.wallpaper = SharedPreferencesRepository.INSTANCE
                    .loadPreference(mContext, Constants.PREF_WALLPAPER, Constants.DEFAULT_PEN_COLOR);
        } else {
            Utils.defaultWallpaper();
        }
        if (SharedPreferencesRepository.INSTANCE.loadPreference(mContext, Constants.ID_PREF_COLOR, false)) {
            Utils.penColor = SharedPreferencesRepository.INSTANCE
                    .loadPreference(mContext, Constants.PREF_COLOR, Constants.DEFAULT_PEN_COLOR);
        } else {
            Utils.defaultColor();
        }
        if (SharedPreferencesRepository.INSTANCE.loadPreference(mContext, Constants.ID_PREF_STROKE, false)) {
            Utils.minStroke = SharedPreferencesRepository.INSTANCE
                    .loadPreference(mContext, Constants.PREF_MIN_TROKE, Constants.DEFAULT_MIN_TROKE);
            Utils.maxStroke = SharedPreferencesRepository.INSTANCE
                    .loadPreference(mContext, Constants.PREF_MAX_TROKE, Constants.DEFAULT_MAX_STROKE);
        } else {
            Utils.defaultStroke();
        }
        if (SharedPreferencesRepository.INSTANCE.loadPreference(mContext, Constants.ID_PREF_ADVERTISING, false)) {
            Utils.disableAds = true;
        } else {
            Utils.defaultDisableAds();
        }
        if (SharedPreferencesRepository.INSTANCE.loadPreference(mContext, Constants.ID_PREF_NAME, false)) {
            Utils.nameSave = true;
        } else {
            Utils.defaultNameSave();
        }
        if (SharedPreferencesRepository.INSTANCE.loadPreference(mContext, Constants.ID_PREF_DELETE, false)) {
            Utils.deleteExit = true;
        } else {
            Utils.defaultDeleteExit();
        }
    }

    public static void saveAllPreferences(Context mContext) {
        if (SharedPreferencesRepository.INSTANCE.loadPreference(mContext, Constants.ID_PREF_COLOR, false)) {
            SharedPreferencesRepository.INSTANCE.savePreference(mContext, Constants.PREF_COLOR, Utils.penColor);
        }
        if (SharedPreferencesRepository.INSTANCE.loadPreference(mContext, Constants.ID_PREF_STROKE, false)) {
            SharedPreferencesRepository.INSTANCE.savePreference(mContext, Constants.PREF_MIN_TROKE, Utils.minStroke);
            SharedPreferencesRepository.INSTANCE.savePreference(mContext, Constants.PREF_MAX_TROKE, Utils.maxStroke);
        }
        if (SharedPreferencesRepository.INSTANCE.loadPreference(mContext, Constants.ID_PREF_WALLPAPER, false)) {
            SharedPreferencesRepository.INSTANCE.savePreference(mContext, Constants.PREF_WALLPAPER, Utils.wallpaper);
        }
    }

    public static void shareSign(Activity mActivity, String name) {

        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = Uri.parse(new File(Utils.path + "/" + name).toString());
        } else {
            imageUri = Uri.fromFile(new File(Utils.path + "/" + name));
        }

        if (name.contains(".png") || name.contains(".PNG")) {
           /* Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/*");
            mActivity.startActivity(Intent.createChooser(shareIntent, mActivity.getText(R.string.tittle_send)));*/
            // create new Intent
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider
                    .getUriForFile(mActivity, BuildConfig.APPLICATION_ID, new File(Utils.path + "/" + name));
            intent.setDataAndType(uri, "image/*");
            PackageManager pm = mActivity.getPackageManager();
            if (intent.resolveActivity(pm) != null) {
                mActivity.startActivity(intent);
            }

        } else {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider
                    .getUriForFile(mActivity, BuildConfig.APPLICATION_ID, new File(Utils.path + "/" + name));
            intent.setDataAndType(uri, "text/html");
            PackageManager pm = mActivity.getPackageManager();
            if (intent.resolveActivity(pm) != null) {
                mActivity.startActivity(intent);
            }
        }
    }


    public static void sort(List<ItemFile> list, final int type) {
        Collections.sort(list, (lhs, rhs) -> {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String date1 = lhs.getDate();
            String date2 = rhs.getDate();
            Date ddate = null;
            Date ddate2 = null;
            try {
                ddate = formatter.parse(date1);
                ddate2 = formatter.parse(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return ddate.compareTo(ddate2) * type;
        });

    }


    private Utils() {
    }

}