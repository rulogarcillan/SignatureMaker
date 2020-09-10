package com.signaturemaker.app.application.core.platform

import android.app.Activity
import android.graphics.Bitmap
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
    private fun createFolder(path: String) {
        val file = File(path)
        if (!file.isDirectory) {
            file.mkdirs()
        }
    }

    /**
     * Remove all files
     */
    fun deleteAllFiles() {
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
                }
            }
        }
    }

    fun generateName(): String {

        return systemTime()
    }

    private fun getFile(name: String): File {
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

    fun moveFiles(oldPath: String) {

        if (oldPath != Utils.path) {
            val files: Array<File>?
            val folder = File(oldPath)

            if (folder.exists()) {
                files = folder.listFiles()
                files?.let {
                    for (file in files) {

                        val name = file.name

                        if ((name.contains(".png") || name.contains(".PNG") || name.contains(".svg") || name
                                .contains(".SVG")) && name.substring(0, 2) == "SM"
                        ) {

                            file.renameTo(File(Utils.path + "/" + name))
                        }
                    }
                }
            }
        }
    }

    fun removeFileByName(name: String) {
        val file = getFile(name)
        if (file.exists()) {
            file.delete()
        }
    }

    fun removeFile(path: String) {
        val file = File(path)
        if (file.exists()) {
            file.delete()
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
    private fun systemTime(): String {

        val date = Date()
        val dfd = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        val dfh = SimpleDateFormat("HHmmss", Locale.getDefault())

        return dfd.format(date) + "_" + dfh.format(date)
    }
}






