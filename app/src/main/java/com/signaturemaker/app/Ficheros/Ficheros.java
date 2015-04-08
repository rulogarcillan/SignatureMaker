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

import static com.signaturemaker.app.Constantes.PreferencesCons.PATH_OLDER_VERSION;
import static com.signaturemaker.app.Constantes.PreferencesCons.PATH_SAVE_ORIGINAL;
import static com.signaturemaker.app.Constantes.PreferencesCons.pathFiles;
import static com.signaturemaker.app.Nucleo.LogUtils.LOGE;
import static com.signaturemaker.app.Nucleo.LogUtils.TRAZA;

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
        archivo = new File(pathFiles + "/" + nombre);
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

        if (bm == null) {
            TRAZA("Imagen totalmente trasparente, lanza mensaje error");
            return false;
        }

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

        Bitmap retorno = null;

        int[] pixels = new int[picToCrop.getHeight() * picToCrop.getWidth()];
        int marginTop = -1, marginBottom = -1, marginLeft = -1, marginRight = -1, i;
        picToCrop.getPixels(pixels, 0, picToCrop.getWidth(), 0, 0,
                picToCrop.getWidth(), picToCrop.getHeight());

        TRAZA("PIXELES1 " + marginLeft + " " + marginRight + " " + marginBottom + " " + marginTop);
        for (i = 0; i < pixels.length; i++) {
            if (pixels[i] != unusedSpaceColor) {
                marginTop = i / picToCrop.getWidth();
                break;
            }
        }


        if (marginTop < 0) { //bitmap totalmente vacio
            return null;
        }
        TRAZA("PIXELES2 " + marginLeft + " " + marginRight + " " + marginBottom + " " + marginTop);
        outerLoop1:
        for (i = 0; i < picToCrop.getWidth(); i++) {
            for (int j = i; j < pixels.length; j += picToCrop.getWidth()) {
                if (pixels[j] != unusedSpaceColor) {
                    marginLeft = j % picToCrop.getWidth();
                    break outerLoop1;
                }
            }
        }
        TRAZA("PIXELES3 " + marginLeft + " " + marginRight + " " + marginBottom + " " + marginTop);
        for (i = pixels.length - 1; i >= 0; i--) {
            if (pixels[i] != unusedSpaceColor) {
                marginBottom = (pixels.length - i) / picToCrop.getWidth();
                break;
            }
        }
        TRAZA("PIXELES4 " + marginLeft + " " + marginRight + " " + marginBottom + " " + marginTop);
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

        TRAZA("PIXELES5 " + marginLeft + " " + marginRight + " " + marginBottom + " " + marginTop);


        retorno = Bitmap.createBitmap(picToCrop, marginLeft, marginTop,
                picToCrop.getWidth() - marginLeft - marginRight,
                picToCrop.getHeight() - marginTop - marginBottom);

        return retorno;
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
                    File noMedia = new File(pathFiles + "/", ".nomedia");
                    TRAZA("URI " + Uri.fromFile(noMedia));
                    if (noMedia.exists()) {

                        return true;
                    }

                    FileOutputStream noMediaOutStream = new FileOutputStream(noMedia);
                    noMediaOutStream.write(0);
                    noMediaOutStream.close();
                    TRAZA("Creamos nomedia");
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

                    File noMedia = new File(pathFiles + "/", ".nomedia");
                    TRAZA("URI " + Uri.fromFile(noMedia));
                    if (noMedia.exists()) {

                        noMedia.delete();
                        TRAZA("Eliminamos nomedia");
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


    public static void deleteAllFiles(Activity activity) {

        File archivos[];
        File carpeta;

        carpeta = new File(pathFiles);
        if (carpeta.exists()) {
            archivos = carpeta.listFiles();
            for (File file : archivos) {

                String name = file.getName();

                if ((name.contains(".png") || name.contains(".PNG")) && name.substring(0, 2).equals("SM")) {
                    file.delete();
                    activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                    TRAZA("URI " + Uri.fromFile(file));
                    TRAZA("Eliminamos " + name);
                }
            }
        }
    }

    public static void moveFiles(String oldPaht, Activity activity) {

        if (!oldPaht.equals(pathFiles)) {

            TRAZA("Ruta antigua" + oldPaht);
            TRAZA("Ruta nueva" + pathFiles);

            File archivos[];
            File carpeta;

            carpeta = new File(oldPaht);
            if (carpeta.exists()) {
                archivos = carpeta.listFiles();
                for (File file : archivos) {

                    String name = file.getName();

                    if (((name.contains(".png") || name.contains(".PNG")) && name.substring(0, 2).equals("SM")) || name.equals(".nomedia")) {
                        file.renameTo(new File(pathFiles + "/" + name));
                        TRAZA("Movemos el fichero" + name);
                        TRAZA("URI " + Uri.fromFile(new File(pathFiles + "/" + name)));
                        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file))); //antigua
                        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(pathFiles + "/" + name)))); //nueva
                    }
                }
            }
        }
    }


    public static void moveFilesVersionAntigua(Activity activity) {

        TRAZA("Ruta version antigua" + PATH_OLDER_VERSION);
        TRAZA("Ruta version nueva" + PATH_SAVE_ORIGINAL);

        File archivos[];
        File carpeta;

        carpeta = new File(PATH_OLDER_VERSION);
        if (carpeta.exists()) {
            archivos = carpeta.listFiles();
            for (File file : archivos) {

                String name = file.getName();

                if (name.contains(".png") || name.contains(".PNG")) {
                    file.renameTo(new File(PATH_SAVE_ORIGINAL + "/" + "SM_" + name));
                    TRAZA("Movemos el fichero" + name);
                    TRAZA("URI " + Uri.fromFile(new File(PATH_SAVE_ORIGINAL + "/" + name)));
                    activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file))); //antigua
                    activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(PATH_SAVE_ORIGINAL + "/" + "SM_" + name)))); //nueva
                }
            }
            carpeta.delete();
        }
    }

}