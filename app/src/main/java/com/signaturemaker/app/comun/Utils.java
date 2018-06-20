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
package com.signaturemaker.app.comun;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;

public final class Utils {

    private Utils() {
    }

    /**
     * Get last time compilation app
     *
     * @param context
     * @return
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

    /**
     * Show toats
     * @param mContext
     * @param msg
     * @param duration
     */
    public static void showToast(Context mContext, String msg, int duration) {
        Toast.makeText(mContext, msg, duration).show();
    }

    /**
     * Show toats, default short duration
     * @param mContext
     * @param msg
     *
     */
    public static void showToast(Context mContext, String msg) {
        showToast(mContext, msg, Toast.LENGTH_SHORT);
    }
}