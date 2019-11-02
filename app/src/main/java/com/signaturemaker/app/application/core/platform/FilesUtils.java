package com.signaturemaker.app.application.core.platform;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import com.signaturemaker.app.application.core.extensions.Utils;
import com.signaturemaker.app.domain.models.ItemFile;
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
import java.util.Locale;

public final class FilesUtils {


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

    /**
     * Remove all files
     */
    public static void deleteAllFiles(Activity mActivity) {
        File[] files;
        File folder;
        folder = new File(Utils.INSTANCE.getPath());
        if (folder.exists()) {
            files = folder.listFiles();
            for (File file : files) {
                String name = file.getName();
                if ((name.contains(".png") || name.contains(".PNG") || name.contains(".svg") || name.contains(".SVG"))
                        && name.substring(0, 2).equals("SM")) {
                    file.delete();
                    deleteScanFile(mActivity, file);
                    //activity.getContentResolver().delete(Uri.parse(file.getAbsolutePath()), null, null);
                    //activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));

                }
            }

        }
    }

    public static String generateName() {

        String name = systemTime();
        return name;
    }

    public static File getFile(String name) {
        return new File(Utils.INSTANCE.getPath() + name);
    }

    public static List loadItemsFiles() {

        File[] files;
        File folder;

        List<ItemFile> arrayItems = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        folder = new File(Utils.INSTANCE.getPath());
        if (folder.exists()) {
            files = folder.listFiles();

            for (File file : files) {
                java.util.Date myDate = new java.util.Date(file.lastModified());
                String tam = file.length() / 1024 + " KB";
                String name = file.getName();

                if ((name.contains(".png") || name.contains(".PNG") || name.contains(".svg") || name.contains(".SVG"))
                        && name.substring(0, 3).equals("SM_")) {
                    ItemFile item = new ItemFile(name, dateFormat.format(myDate), tam);
                    arrayItems.add(item);
                }
            }
        }
        Utils.INSTANCE.sort(arrayItems, Utils.INSTANCE.getSortOrder());
        return arrayItems;
    }

    public static void moveFiles(String oldPaht, Activity mActivity) {

        if (!oldPaht.equals(Utils.INSTANCE.getPath())) {
            File[] files;
            File folder;

            folder = new File(oldPaht);
            if (folder.exists()) {
                files = folder.listFiles();
                for (File file : files) {

                    String name = file.getName();

                    if (((name.contains(".png") || name.contains(".PNG") || name.contains(".svg") || name
                            .contains(".SVG")) && name.substring(0, 2).equals("SM")) || name.equals(".nomedia")) {

                        file.renameTo(new File(Utils.INSTANCE.getPath() + "/" + name));
                        deleteScanFile(mActivity, file);
                        // scanFile(mActivity, new File(Utils.path + "/" + name));
                        //mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath()))); //antigua
                        mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.parse(Utils.INSTANCE.getPath() + "/" + name))); //nueva
                    }

                }
            }
        }
    }

    public static Boolean noMedia(Activity mActivity) {
        {
            String storageState = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(storageState)) {
                try {
                    File noMedia = new File(Utils.INSTANCE.getPath(), ".nomedia");

                    if (noMedia.exists()) {
                        scanFile(mActivity, noMedia);
                        return true;
                    }
                    scanFile(mActivity, noMedia);
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

    public static Boolean noMediaRemove(Activity mActivity) {
        {
            String storageState = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(storageState)) {
                try {
                    File noMedia = new File(Utils.INSTANCE.getPath(), ".nomedia");
                    if (noMedia.exists()) {
                        noMedia.delete();
                        scanFile(mActivity, noMedia);

                        File folder = new File(Utils.INSTANCE.getPath());
                        File[] files;
                        if (folder.exists()) {
                            files = folder.listFiles();
                            for (File file : files) {
                                mActivity.getContentResolver().delete(Uri.parse(file.getAbsolutePath()), null, null);
                            }
                        }

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

    public static void removeFile(Activity mActivity, String name) {
        File file = getFile(name);
        if (file.exists()) {
            file.delete();
            deleteScanFile(mActivity, file);
        }
    }

    public static boolean saveBitmapFile(Bitmap bitmap, String name) {

        File file;
        createFolder(Utils.INSTANCE.getPath());
        file = new File(Utils.INSTANCE.getPath() + name);

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
        createFolder(Utils.INSTANCE.getPath());
        file = new File(Utils.INSTANCE.getPath() + name);

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
     * rturn a string -->>> ddMMyyyy_hhmmss
     */
    public static String systemTime() {

        Date date = new Date();
        DateFormat dfd = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        DateFormat dfh = new SimpleDateFormat("HHmmss", Locale.getDefault());
        String dateString = dfd.format(date) + "_" + dfh.format(date);

        return dateString;

    }

    private static void deleteScanFile(final Activity mActivity, File file) {
        if (mActivity != null) {
            ContentResolver resolver = mActivity.getContentResolver();
            resolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?",
                    new String[]{file.getPath()});
            mActivity.sendBroadcast(
                    new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getPath()))); //nueva
        }
    }

    private static void scanFile(Activity mActivity, File file) {
        mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)));
    }
}






