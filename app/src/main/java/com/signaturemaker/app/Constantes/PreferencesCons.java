package com.signaturemaker.app.Constantes;

import android.graphics.Color;
import android.os.Environment;

/**
 * Created by rulo on 19/03/15.
 */
public final class PreferencesCons {

    public static final String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static final int TRAZO_GROSOR_ORIGINAL = 3;

    public static final int TRAZO_COLOR_ORIGINAL = Color.parseColor("#200BB2");

    public static final String PATH_SAVE_ORIGINAL = ROOT + "/Signature maker/";
    public static final String PATH_OLDER_VERSION = ROOT + "/Signature maker/.images/";

    public static int strokeTrazo = TRAZO_GROSOR_ORIGINAL;
    public static int colorTrazo = TRAZO_COLOR_ORIGINAL;
    public static String pathFiles = PATH_SAVE_ORIGINAL;


    public static void valoresPorDefecto() {

        strokeTrazo = TRAZO_GROSOR_ORIGINAL;
        colorTrazo = TRAZO_COLOR_ORIGINAL;
        pathFiles = PATH_SAVE_ORIGINAL;

    }

}
