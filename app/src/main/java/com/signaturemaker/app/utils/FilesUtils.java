package com.signaturemaker.app.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.signaturemaker.app.models.ItemFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class FilesUtils {


    public static String generateName() {

        String name = systemTime();
        return name;
    }

    public static String addExtensionNamePng(String name) {

        name = "SM_" + name + ".png";
        return name;
    }

    public static String addExtensionNameSvg(String name) {

        name = "SM_" + name + ".svg";
        return name;
    }


    public static String cleanName(String name) {

        String original = " áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ.\\/:?*\"<>|";
        String ascii = "_aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC          ";
        String output = name;
        for (int i = 0; i < original.length(); i++) {
            output = output.replace(original.charAt(i), ascii.charAt(i)).toLowerCase().trim();
        }
        return output;
    }


    /**
     * rturn a string -->>> ddMMyyyy_hhmmss
     */
    public static String systemTime() {

        Date date = new Date();
        DateFormat dfd = new SimpleDateFormat("ddMMyyyy");
        DateFormat dfh = new SimpleDateFormat("HHmmss");
        String dateString = dfd.format(date) + "_" + dfh.format(date);

        return dateString;

    }

    public static boolean saveBitmapFile(Bitmap bitmap, String name) {

        File file;
        createFolder(Utils.path);
        file = new File(Utils.path + name);

        try {
            file.createNewFile();
            FileOutputStream os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean saveSvgFile(String content, String name) {

        File file;
        createFolder(Utils.path);
        file = new File(Utils.path + name);

        try {
            // file.createNewFile();
            //FileOutputStream os = new FileOutputStream(file);
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            try {
                out.write(content);
            } catch (IOException e) {
                return false;
            } finally {
                out.close();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Create a new folder
     *
     * @param path name of folder (path complete)
     */
    public static void createFolder(String path) {
        File file = new File(path);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
    }


    public static List loadItemsFiles() {

        File files[];
        File folder;

        List<ItemFile> arrayItems = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        folder = new File(Utils.path);
        if (folder.exists()) {
            files = folder.listFiles();

            for (File file : files) {
                java.util.Date myDate = new java.util.Date(file.lastModified());
                String tam = Long.toString(file.length() / 1024) + " KB";
                String name = file.getName();

                if ((name.contains(".png") || name.contains(".PNG") || name.contains(".svg") || name.contains(".SVG")) && name.substring(0, 3).equals("SM_")) {
                    ItemFile item = new ItemFile(name, dateFormat.format(myDate), tam);
                    arrayItems.add(item);
                }
            }
        }
        Utils.sort(arrayItems, Utils.sortOrder);
        return arrayItems;
    }


    public static void moveFiles(String oldPaht, Activity mActivity) {

        if (!oldPaht.equals(Utils.path)) {
            File files[];
            File folder;

            folder = new File(oldPaht);
            if (folder.exists()) {
                files = folder.listFiles();
                for (File file : files) {

                    String name = file.getName();

                    if (((name.contains(".png") || name.contains(".PNG") || name.contains(".svg") || name.contains(".SVG")) && name.substring(0, 2).equals("SM")) || name.equals(".nomedia")) {
                        file.renameTo(new File(Utils.path + "/" + name));
                        mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file))); //antigua
                        mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(Utils.path + "/" + name)))); //nueva
                    }
                }
            }
        }
    }

    /**
     * Remove all files
     *
     * @param activity
     */
    public static void deleteAllFiles(Activity activity) {
        File files[];
        File folder;
        folder = new File(Utils.path);
        if (folder.exists()) {
            files = folder.listFiles();
            for (File file : files) {
                String name = file.getName();
                if ((name.contains(".png") || name.contains(".PNG") || name.contains(".svg") || name.contains(".SVG")) && name.substring(0, 2).equals("SM")) {
                    file.delete();
                    activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                }
            }
        }
    }

    public static File getFile(String name) {
        return new File(Utils.path + name);
    }




    /*
     *//**
     * Realiza la carga del arrayItems (recoge todos los datos necesario de los
     * ficheros)
     *//*


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



/*


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






*/
}