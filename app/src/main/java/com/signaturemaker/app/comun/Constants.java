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


import android.graphics.Color;
import android.os.Environment;

public final class Constants {

    public static final String TAG = "RULO";


    public static final String OP1 = "OP1";
    public static final String OP2 = "OP2";
    public static final String OP3 = "OP3";
    public static final String OP4 = "OP4";
    public static final String OP5 = "OP5";
    public static final String OP7 = "OP7";
    public static final String OP8 = "OP8";
    public static final String OP9 = "OP9";
    public static final String OPPUB = "OPPUB";
    public static final String FRAG = "FRAG";
    public static final String STROKE = "STROKE";
    public static final String COLOR = "COLOR";


    public static final String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static final int DEFAULT_SHORT_ORDER = -1;
    public static final int DEFAULT_MAX_STROKE = 4;
    public static final int DEFAULT_MINS_TROKE = 1;
    public static final int DEFAULT_PEN_COLOR = Color.parseColor("#200BB2");
    public static final int DEFAULT_WALLPAPER = 3;
    public static final String DEFAULT_PATH = ROOT + "/Signature/";

    public static int shortOrder = DEFAULT_SHORT_ORDER;
    public static int maxStroke = DEFAULT_MAX_STROKE;
    public static int minStroke = DEFAULT_MINS_TROKE;
    public static int penColor = DEFAULT_PEN_COLOR;
    public static int wallpaper = DEFAULT_WALLPAPER;
    public static String path = DEFAULT_PATH;

    public static void defaultValues() {
        shortOrder = DEFAULT_SHORT_ORDER;
        maxStroke = DEFAULT_MAX_STROKE;
        minStroke = DEFAULT_MINS_TROKE;
        penColor = DEFAULT_PEN_COLOR;
        wallpaper = DEFAULT_WALLPAPER;
        path = DEFAULT_PATH;
    }
}