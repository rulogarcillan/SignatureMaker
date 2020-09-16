package com.signaturemaker.app.application.core.platform

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.PNG
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.provider.MediaStore.Images.Media
import android.provider.MediaStore.MediaColumns
import androidx.annotation.RequiresApi
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.domain.models.ItemFile
import com.tuppersoft.skizo.core.extension.logd
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.OutputStream
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

    fun generateName(): String {
        return systemTime()
    }

    /**
     * Create a new folder
     *
     * @param path name of folder (path complete)
     */
    private fun createFolder(path: String) {
        val file = File(path)
        if (!file.isDirectory) {
            file.mkdirs()
        }
    }

    fun getFile(name: String): File {
        return File(Utils.path + File.separator + name)
    }

    /**
     * Remove all files
     */
    fun deleteAllFiles(mContext: Context?) {
        val files: Array<File>?
        val folder = File(Utils.path)
        if (folder.exists()) {
            files = folder.listFiles()
            for (file in files) {
                val name = file.name
                if ((name.contains(".png")
                            || name.contains(".PNG")
                            || name.contains(".svg")
                            || name.contains(".SVG"))
                    && name.startsWith("SM")
                ) {
                    file.delete()
                    callScanIntent(mContext, file.path)
                }
            }
        }
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

    fun moveFiles(mContext: Context?, oldPath: String) {

        if (oldPath != Utils.path) {
            val files: Array<File>?
            val folder = File(oldPath)

            if (folder.exists()) {
                files = folder.listFiles()
                files?.let {
                    for (file in files) {

                        val name = file.name
                        val fileDest = File(Utils.path + File.separator + name)

                        if ((name.contains(".png") || name.contains(".PNG") || name.contains(".svg") || name
                                .contains(".SVG")) && name.substring(0, 2) == "SM"
                        ) {
                            file.renameTo(fileDest)
                            callScanIntent(mContext, file.path)
                        }
                    }
                }
            }
        }
    }

    fun removeFile(mContext: Context?, path: String) {
        val file = File(path)
        if (file.exists()) {
            file.delete()
            callScanIntent(mContext, file.path)
        }
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
    private fun systemTime(): String {

        val date = Date()
        val dfd = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        val dfh = SimpleDateFormat("HHmmss", Locale.getDefault())

        return dfd.format(date) + "_" + dfh.format(date)
    }

    @Throws(IOException::class)
    fun saveBitmap(
        context: Context,
        bitmap: Bitmap?,
        displayName: String
    ): Boolean {

        return if (VERSION.SDK_INT >= VERSION_CODES.Q) {
            saveImageForAndroid10(
                context,
                bitmap,
                displayName
            )
        } else {
            saveImageForLessAndroid10(context, bitmap, displayName)
        }
    }

    @RequiresApi(VERSION_CODES.Q)
    private fun saveImageForAndroid10(
        context: Context,
        bitmap: Bitmap?,
        displayName: String
    ): Boolean {
        if (bitmap == null) {
            return false
        }

        var stream: OutputStream? = null
        val contentValues = ContentValues()
        contentValues.put(MediaColumns.DISPLAY_NAME, displayName)
        contentValues.put(MediaColumns.MIME_TYPE, "image/png")
        contentValues.put(MediaColumns.RELATIVE_PATH, Utils.path)
        val resolver: ContentResolver = context.contentResolver
        var uri: Uri? = null
        try {
            val contentUri: Uri = Media.EXTERNAL_CONTENT_URI
            uri = resolver.insert(contentUri, contentValues)
            if (uri == null) {
                return false
            }
            stream = resolver.openOutputStream(uri)
            if (stream == null) {
                //throw IOException("Failed to get output stream.")
                return false
            }

            if (!bitmap.compress(PNG, 100, stream)) {
                // throw IOException("Failed to save bitmap.")
                return false
            }
        } catch (e: IOException) {
            if (uri != null) {
                // Don't leave an orphan entry in the MediaStore
                resolver.delete(uri, null, null)
            }
            //throw e
            return false
        } finally {
            stream?.close()
        }
        return true
    }

    private fun saveImageForLessAndroid10(context: Context?, bitmap: Bitmap?, name: String): Boolean {
        if (bitmap == null) {
            return false
        }
        val file = File(Utils.path + File.separator + name)

        try {
            createFolder(Utils.path)
            file.createNewFile()
            val os = FileOutputStream(file)
            bitmap.compress(PNG, 100, os)
            os.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        callScanIntent(context, file.path)
        return true
    }

    private fun callScanIntent(context: Context?, filePath: String) {
        context?.let { mContext ->
            MediaScannerConnection.scanFile(
                mContext, arrayOf(filePath), null
            ) { path, _ -> "Scan complete for: $path".logd() }
        }
    }
}






