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
package com.signaturemaker.app.utils;


import android.graphics.Color;
import android.os.Environment;

public final class Constants {

    public static final String TAG = "RULO";

    public static final String ID_PREF_ADVERTISING = "id_pref_advertising";
    public static final String ID_PREF_PATH = "id_pref_path";
    public static final String ID_PREF_DELETE = "id_pref_delete";
    public static final String ID_PREF_GALLERY = "id_pref_gallery";
    public static final String ID_PREF_NAME = "id_pref_name";
    public static final String ID_PREF_COLOR = "id_pref_color";
    public static final String ID_PREF_STROKE = "id_pref_stroke";
    public static final String ID_PREF_RESET = "id_pref_reset";

    public static final String OPPUB = "OPPUB";
    public static final String FRAG = "FRAG";
    public static final String PREF_MAX_TROKE = "MAX_STROKE";
    public static final String PREF_MIN_TROKE = "MIN_STROKE";
    public static final String PREF_COLOR = "COLOR";

    public static final String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static final int DEFAULT_SORT_ORDER = -1;
    public static final int DEFAULT_MAX_STROKE = 4;
    public static final int DEFAULT_MIN_TROKE = 1;
    public static final int DEFAULT_PEN_COLOR = Color.parseColor("#200BB2");
    public static final int DEFAULT_WALLPAPER = 3;
    public static final String DEFAULT_PATH = ROOT + "/Signature/";


}