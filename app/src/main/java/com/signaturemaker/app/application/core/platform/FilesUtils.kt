package com.signaturemaker.app.application.core.platform

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.domain.models.ItemFile
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FilesUtils {


    fun addExtensionNamePng(name: String) = "SM_$name.png"
    fun addExtensionNameSvg(name: String) = "SM_$name.svg"

    fun cleanName(name: String): String {

        val original = " áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ.\\/:?*\"<>|"
        val ascii = "_aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC          "
        var output = name

        for (i in original.indices) {
            output = output.replace(original[i], ascii[i]).toLowerCase(Locale.ROOT).trim { it <= ' ' }
        }
        return output
    }

    /**
     * Create a new folder
     *
     * @param path name of folder (path complete)
     */
    fun createFolder(path: String) {
        val file = File(path)
        if (!file.isDirectory) {
            file.mkdirs()
        }
    }

    /**
     * Remove all files
     */
    fun deleteAllFiles(mActivity: Activity) {
        val files: Array<File>?
        val folder = File(Utils.path)
        if (folder.exists()) {
            files = folder.listFiles()
            for (file in files) {
                val name = file.name
                if ((name.contains(".png") || name.contains(".PNG") || name.contains(".svg") || name.contains(".SVG")) && name.substring(
                        0,
                        2
                    ) == "SM"
                ) {
                    file.delete()
                    deleteScanFile(mActivity, file)
                }
            }
        }
    }

    fun generateName(): String {

        return systemTime()
    }

    fun getFile(name: String): File {
        return File(Utils.path + name)
    }

    fun loadItemsFiles(): MutableList<ItemFile> {

        val files: Array<File>?
        val folder = File(Utils.path)

        val arrayItems = mutableListOf<ItemFile>()

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        if (folder.exists()) {
            files = folder.listFiles()
            files?.let {
                for (file in files) {
                    val myDate = Date(file.lastModified())
                    val tam = (file.length() / 1024).toString() + " KB"
                    val name = file.name

                    if ((name.contains(".png") || name.contains(".PNG") || name.contains(".svg") || name.contains(".SVG")) && name.substring(
                            0,
                            3
                        ) == "SM_"
                    ) {
                        val item = ItemFile(name, dateFormat.format(myDate), tam)
                        arrayItems.add(item)
                    }
                }
            }
        }

        Utils.sort(arrayItems, Utils.sortOrder)
        return arrayItems
    }

    fun moveFiles(oldPath: String, mActivity: Activity) {

        if (oldPath != Utils.path) {
            val files: Array<File>?
            val folder = File(oldPath)

            if (folder.exists()) {
                files = folder.listFiles()
                files?.let {
                    for (file in files) {

                        val name = file.name

                        if ((name.contains(".png") || name.contains(".PNG") || name.contains(".svg") || name
                                .contains(".SVG")) && name.substring(0, 2) == "SM" || name == ".nomedia"
                        ) {

                            file.renameTo(File(Utils.path + "/" + name))
                            deleteScanFile(mActivity, file)
                            mActivity.sendBroadcast(
                                Intent(
                                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                    Uri.parse(Utils.path + "/" + name)
                                )
                            ) //nueva
                        }
                    }
                }
            }
        }
    }

    fun noMedia(mActivity: Activity): Boolean {
        run {
            val storageState = Environment.getExternalStorageState()

            if (Environment.MEDIA_MOUNTED == storageState) {
                try {
                    val noMedia = File(Utils.path, ".nomedia")

                    if (noMedia.exists()) {
                        scanFile(mActivity, noMedia)
                        return true
                    }
                    scanFile(mActivity, noMedia)
                    val noMediaOutStream = FileOutputStream(noMedia)
                    noMediaOutStream.write(0)
                    noMediaOutStream.close()
                } catch (e: Exception) {
                    return false
                }
            } else {
                return false
            }

            return true

        }
    }

    fun noMediaRemove(mActivity: Activity): Boolean {
        run {
            val storageState = Environment.getExternalStorageState()

            if (Environment.MEDIA_MOUNTED == storageState) {
                try {
                    val noMedia = File(Utils.path, ".nomedia")
                    if (noMedia.exists()) {
                        noMedia.delete()
                        scanFile(mActivity, noMedia)

                        val folder = File(Utils.path)
                        val files: Array<File>?
                        if (folder.exists()) {
                            files = folder.listFiles()
                            files?.let {
                                for (file in files) {
                                    mActivity.contentResolver.delete(Uri.parse(file.absolutePath), null, null)
                                }
                            }
                        }

                        return true
                    }
                } catch (e: Exception) {
                    return false
                }
            } else {
                return false
            }
            return true

        }
    }

    fun removeFile(mActivity: Activity, name: String) {
        val file = getFile(name)
        if (file.exists()) {
            file.delete()
            deleteScanFile(mActivity, file)
        }
    }

    fun saveBitmapFile(bitmap: Bitmap?, name: String): Boolean {
        if (bitmap == null) {
            return false
        }
        try {
            val file = File(Utils.path + name)
            createFolder(Utils.path)
            file.createNewFile()
            val os = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
            os.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun saveSvgFile(content: String, name: String): Boolean {

        val file = File(Utils.path + name)
        createFolder(Utils.path)

        try {
            // file.createNewFile();
            //FileOutputStream os = new FileOutputStream(file);
            val out = BufferedWriter(FileWriter(file))

            try {
                out.write(content)
            } catch (e: IOException) {
                return false
            } finally {
                out.close()
            }
        } catch (e: Exception) {
            return false
        }

        return true
    }

    /**
     * rturn a string -->>> ddMMyyyy_hhmmss
     */
    fun systemTime(): String {

        val date = Date()
        val dfd = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        val dfh = SimpleDateFormat("HHmmss", Locale.getDefault())

        return dfd.format(date) + "_" + dfh.format(date)
    }

    private fun deleteScanFile(mActivity: Activity?, file: File) {
        if (mActivity != null) {
            val resolver = mActivity.contentResolver
            resolver.delete(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?",
                arrayOf(file.path)
            )
            mActivity.sendBroadcast(
                Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.path))
            ) //nueva
        }
    }

    private fun scanFile(mActivity: Activity, file: File) {
        mActivity.sendBroadcast(
            Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)
            )
        )
    }
}






