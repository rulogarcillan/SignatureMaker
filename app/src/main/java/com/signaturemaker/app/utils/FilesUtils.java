package com.signaturemaker.app.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

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
                        mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath()))); //antigua
                        mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(Utils.path + "/" + name))); //nueva
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
                    activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));
                }
            }
        }
    }

    public static File getFile(String name) {
        return new File(Utils.path + name);
    }

    public static void removeFile(String name) {
        File file = getFile(name);
        if (file.exists()) {
            file.delete();
        }
    }


    public static Boolean noMedia() {
        {
            String storageState = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(storageState)) {
                try {
                    File noMedia = new File(Utils.path, ".nomedia");

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


    public static Boolean noMediaRemove() {
        {
            String storageState = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(storageState)) {
                try {
                    File noMedia = new File(Utils.path, ".nomedia");
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


    /**
     * Remove all files
     *
     * @param activity
     */
    public static void reScan(Activity activity) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            final Uri contentUri = Uri.fromFile(new File("file://" + Environment.getExternalStorageDirectory()));
            scanIntent.setData(contentUri);
            activity.sendBroadcast(scanIntent);
        } else {
            activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
        moveFilesRe(Utils.path, Utils.path+"Bis/", activity);
    }

    public static void moveFilesRe(String oldPaht,String newPaht, Activity mActivity) {

        if (!oldPaht.equals(newPaht)) {
            File files[];
            File folder;


            folder = new File(oldPaht);
            createFolder(newPaht);
            if (folder.exists()) {
                files = folder.listFiles();
                for (File file : files) {

                    String name = file.getName();

                    if (((name.contains(".png") || name.contains(".PNG") || name.contains(".svg") || name.contains(".SVG")) && name.substring(0, 2).equals("SM")) || name.equals(".nomedia")) {
                        file.renameTo(new File(newPaht + name));
                        mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath()))); //antigua
                        mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(newPaht  + name))); //nueva
                    }
                }
            }
        }

        if (!newPaht.equals(oldPaht)) {
            File files[];
            File folder;

            folder = new File(newPaht);
            if (folder.exists()) {
                files = folder.listFiles();
                for (File file : files) {

                    String name = file.getName();

                    if (((name.contains(".png") || name.contains(".PNG") || name.contains(".svg") || name.contains(".SVG")) && name.substring(0, 2).equals("SM")) || name.equals(".nomedia")) {
                        file.renameTo(new File(oldPaht + name));
                        mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath()))); //antigua
                        mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(oldPaht + name))); //nueva
                    }
                }
            }
        }

        removeFile(newPaht);
    }
}



