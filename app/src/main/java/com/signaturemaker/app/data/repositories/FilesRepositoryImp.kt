package com.signaturemaker.app.data.repositories

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.PNG
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build.VERSION_CODES
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.domain.models.ItemFile
import com.signaturemaker.app.domain.models.error.FileError.CreateError
import com.signaturemaker.app.domain.models.error.FileError.EmptyBitmap
import com.signaturemaker.app.domain.repository.FilesRepository
import com.tuppersoft.skizo.android.core.extension.logd
import com.tuppersoft.skizo.kotlin.core.domain.response.Response
import com.tuppersoft.skizo.kotlin.core.domain.response.Response.onSuccess
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class FilesRepositoryImp @Inject constructor(@ApplicationContext val appContext: Context) : FilesRepository {

    @RequiresApi(VERSION_CODES.Q)
    override suspend fun saveFileBitmapMoreAndroid10(
        bitmap: Bitmap?,
        pathToSave: String,
        displayName: String
    ): Flow<Response<Uri>> {
        if (bitmap == null) {
            return flow { emit(Response.onFailure(EmptyBitmap(appContext.getString(R.string.empty_canvas)))) }
        }
        var stream: OutputStream? = null
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, pathToSave)
        //contentValues.put(MediaStore.MediaColumns.DATE_TAKEN, System.currentTimeMillis())
        val resolver: ContentResolver = appContext.contentResolver
        var uri: Uri? = null
        try {
            val contentUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            uri = contentUri?.let {
                resolver.insert(contentUri, contentValues)
            } ?: return flow { emit(Response.onFailure(CreateError(appContext.getString(R.string.title_save_ko)))) }

            stream = resolver.openOutputStream(uri)
            if (stream == null) {
                return flow { emit(Response.onFailure(EmptyBitmap(appContext.getString(R.string.title_save_ko)))) }
            }

            if (!bitmap.compress(PNG, 100, stream)) {
                return flow { emit(Response.onFailure(EmptyBitmap(appContext.getString(R.string.title_save_ko)))) }
            }
        } catch (e: IOException) {
            if (uri != null) {
                resolver.delete(uri, null, null)
            }
            return flow { emit(Response.onFailure(CreateError(appContext.getString(R.string.title_save_ko)))) }
        } finally {
            stream?.close()
        }
        return flow { emit(Response.onSuccess(uri!!)) }
    }

    override suspend fun saveFileBitmapLessAndroid10(
        bitmap: Bitmap?,
        pathToSave: String,
        displayName: String
    ): Flow<Response<Uri>> {
        if (bitmap == null) {
            return flow { emit(Response.onFailure(EmptyBitmap(appContext.getString(R.string.empty_canvas)))) }
        }
        val file = File(pathToSave + File.separator + displayName)
        try {
            createFolder(pathToSave)
            file.createNewFile()
            val os = FileOutputStream(file)
            bitmap.compress(PNG, 100, os)
            os.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return flow { emit(Response.onFailure(CreateError(appContext.getString(R.string.title_save_ko)))) }
        }

        reloadMediaScanner(file.path).collect()

        val photoURI: Uri = FileProvider.getUriForFile(
            appContext,
            appContext.applicationContext.packageName.toString(),
            file
        )

        return flow { emit(Response.onSuccess(photoURI)) }
    }

    @RequiresApi(VERSION_CODES.Q)
    override suspend fun deleteFileBitmapMoreAndroid10(uri: Uri): Flow<Response<Boolean>> {
        appContext.contentResolver?.delete(
            uri,
            null,
            null
        )
        return flow { emit(Response.onSuccess(true)) }
    }


    override suspend fun deleteFileBitmapLessAndroid10(file: File): Flow<Response<Boolean>> {
        return flow {
            if (file.exists()) {
                file.delete()
                reloadMediaScanner(file.path).collect {
                    emit(onSuccess(true))
                }
            } else {
                emit(onSuccess(true))
            }
        }
    }

    @RequiresApi(VERSION_CODES.Q)
    override suspend fun loadItemsFilesMoreAndroid10(): Flow<Response<List<ItemFile>>> {
        val galleryImageUrls = mutableListOf<ItemFile>()
        val columns = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE,
            MediaStore.Images.Media.DATE_TAKEN
        )
        val orderBy = MediaStore.Images.Media.DATE_TAKEN

        appContext.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            columns,
            MediaStore.Audio.Media.RELATIVE_PATH + " like ? ",
            arrayOf(Utils.path),
            "$orderBy DESC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val displayNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
            val dateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val displayName = cursor.getString(displayNameColumn) ?: "NO_NAME"
                val size = cursor.getInt(sizeColumn)
                val date = cursor.getLong(dateColumn)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ROOT)
                val myDate = Date(date)
                val tam = (size / 1024).toString() + " KB"

                if (displayName.startsWith("SM")) {
                    galleryImageUrls.add(
                        ItemFile(
                            ContentUris.withAppendedId(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                id
                            ), displayName, dateFormat.format(myDate), tam, false
                        )
                    )
                }
            }
        }

        return flow { emit(Response.onSuccess(galleryImageUrls.toList())) }
    }

    override suspend fun loadItemsFilesLessAndroid10(filesPath: String): Flow<Response<List<ItemFile>>> {
        val folder = File(filesPath)
        return if (folder.exists()) {

            val itemListFiles: MutableList<ItemFile> = mutableListOf()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            folder.listFiles()?.toMutableList()?.toList()?.forEach { file ->

                val myDate = Date(file.lastModified())
                val tam = (file.length() / 1024).toString() + " KB"
                val name = file.name

                if ((name.contains(".png") || name.contains(".PNG") || name.contains(".svg") || name.contains(
                        ".SVG"
                    )) && name.startsWith(
                        "SM_"
                    )
                ) {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        appContext,
                        appContext.applicationContext.packageName.toString(),
                        file
                    )
                    itemListFiles.add(ItemFile(photoURI, name, dateFormat.format(myDate), tam))
                }

            }
            Utils.sort(itemListFiles, Utils.sortOrder)
            flow { emit(Response.onSuccess(itemListFiles)) }
        } else {
            flow { emit(Response.onSuccess(emptyList<ItemFile>())) }
        }
    }

    @Deprecated("Only for migration")
    override suspend fun moveFile(oldPath: String, newPath: String, fileName: String): Flow<Response<Boolean>> {
        createFolder(newPath)
        return flow {
            val oldFile = File(oldPath + File.separator + fileName)
            val newFile = File(newPath + File.separator + fileName)

            oldFile.renameTo(newFile)
            reloadMediaScanner(oldFile.path, newFile.path).collect {
                emit(Response.onSuccess(true))
            }
        }
    }

    override suspend fun reloadMediaScanner(vararg filePath: String): Flow<Response<Boolean>> {
        return flow {
            MediaScannerConnection.scanFile(
                appContext, filePath, null
            ) { path, _ ->
                "Scan complete for: $path".logd()
            }
            emit(Response.onSuccess(true))
        }
    }

    private fun createFolder(path: String) {
        val file = File(path)
        if (!file.isDirectory) {
            file.mkdirs()
        }
    }
}
