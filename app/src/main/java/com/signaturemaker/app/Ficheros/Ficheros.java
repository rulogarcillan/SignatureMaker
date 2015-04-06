package com.signaturemaker.app.Ficheros;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;

import com.signaturemaker.app.Nucleo.ItemFile;
import com.signaturemaker.app.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.signaturemaker.app.Constantes.PreferencesCons.pathFiles;
import static com.signaturemaker.app.Nucleo.LogUtils.LOGE;

public final class Ficheros {


    public static String generaNombre() {

        String name = "SM_" + HoraSistema() + ".png";
        return name;
    }


    /**
     * Devuelve un string concatenada la fecha y hora -->>> _ddMMyyyy_hhmmss
     */
    public static String HoraSistema() {

        //
        Date date = new Date();
        DateFormat dfd = new SimpleDateFormat("ddMMyyyy");
        DateFormat dfh = new SimpleDateFormat("HHmmss");

        String fecha = dfd.format(date) + "_" + dfh.format(date);

        return fecha;

    }


    /**
     * Realiza la carga del arrayItems (recoge todos los datos necesario de los
     * ficheros)
     */
    public static ArrayList cargaItems() {

        File archivos[];
        File carpeta;

        ArrayList<ItemFile> arrayItems = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");


        carpeta = new File(pathFiles);
        if (carpeta.exists()) {
            archivos = carpeta.listFiles();

            for (File file : archivos) {

                java.util.Date myDate = new java.util.Date(file.lastModified());
                String tam = Long.toString(file.length() / 1024) + " KB";
                String name = file.getName();

                if ((name.contains(".png") || name.contains(".PNG")) && name.substring(0, 2).equals("SM")) {
                    ItemFile item = new ItemFile(name, dateFormat.format(myDate), tam);
                    arrayItems.add(item);
                }
            }
        }
        return arrayItems;
    }

    public static void removeFile(String nombre) {
        File archivo;
        archivo = new File(pathFiles + nombre);
        if (archivo.exists()) {
            archivo.delete();
        }
    }

    public static File getFile(String nombre) {

        return new File(pathFiles + nombre);

    }

    public static void CreaPath() {

        File file = new File(pathFiles);

        if (!file.isDirectory()) {
            file.mkdirs();
        }
    }


    public static boolean guardar(Bitmap bm2, String name) {

        File file;
        CreaPath();

        Bitmap bm = cropBitmapToBoundingBox(bm2, Color.TRANSPARENT);

        file = new File(pathFiles + "/" + name);

        try {

            file.createNewFile();
            FileOutputStream os = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();

        } catch (Exception e) {

            LOGE("ficheros.guardar", e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;

    }

    //thanks to user grubFX for the code
    //http://stackoverflow.com/questions/6717433/bitmap-conversion-creating-bitmap-that-excludes-transparent-sides-from-transpar
    public static Bitmap cropBitmapToBoundingBox(Bitmap picToCrop, int unusedSpaceColor) {
        int[] pixels = new int[picToCrop.getHeight() * picToCrop.getWidth()];
        int marginTop = 0, marginBottom = 0, marginLeft = 0, marginRight = 0, i;
        picToCrop.getPixels(pixels, 0, picToCrop.getWidth(), 0, 0,
                picToCrop.getWidth(), picToCrop.getHeight());

        for (i = 0; i < pixels.length; i++) {
            if (pixels[i] != unusedSpaceColor) {
                marginTop = i / picToCrop.getWidth();
                break;
            }
        }

        outerLoop1:
        for (i = 0; i < picToCrop.getWidth(); i++) {
            for (int j = i; j < pixels.length; j += picToCrop.getWidth()) {
                if (pixels[j] != unusedSpaceColor) {
                    marginLeft = j % picToCrop.getWidth();
                    break outerLoop1;
                }
            }
        }

        for (i = pixels.length - 1; i >= 0; i--) {
            if (pixels[i] != unusedSpaceColor) {
                marginBottom = (pixels.length - i) / picToCrop.getWidth();
                break;
            }
        }

        outerLoop2:
        for (i = pixels.length - 1; i >= 0; i--) {
            for (int j = i; j >= 0; j -= picToCrop.getWidth()) {
                if (pixels[j] != unusedSpaceColor) {
                    marginRight = picToCrop.getWidth()
                            - (j % picToCrop.getWidth());
                    break outerLoop2;
                }
            }
        }

        return Bitmap.createBitmap(picToCrop, marginLeft, marginTop,
                picToCrop.getWidth() - marginLeft - marginRight,
                picToCrop.getHeight() - marginTop - marginBottom);
    }

    public static void sendFirma(Activity activity, String name) {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(pathFiles + "/" + name)));
        shareIntent.setType("image/*");
        activity.startActivity(Intent.createChooser(shareIntent, activity.getText(R.string.enviarsolo)));

    }

    public static Boolean nomedia() {
        {
            String storageState = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(storageState)) {
                try {
                    File noMedia = new File(pathFiles , ".nomedia");

                    if (noMedia.exists()) {

                        return true;
                    }

                    FileOutputStream noMediaOutStream = new FileOutputStream(noMedia);
                    noMediaOutStream.write(0);
                    noMediaOutStream.close();
                } catch (Exception e) {

                    return false;
                }
            } else {

                return false;
            }

            return true;

        }
    }


    public static Boolean nomediaRemove() {
        {
            String storageState = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(storageState)) {
                try {

                    File noMedia = new File(pathFiles , ".nomedia");

                    if (noMedia.exists()) {

                        noMedia.delete();
                        return true;
                    }

                } catch (Exception e) {

                    return false;
                }
            } else {

                return false;
            }

            return true;

        }
    }
}