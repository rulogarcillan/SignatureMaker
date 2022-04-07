package com.signaturemaker.app.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import com.signaturemaker.app.domain.models.ItemFile
import com.tuppersoft.skizo.kotlin.core.domain.response.Response
import java.io.File
import kotlinx.coroutines.flow.Flow

interface FilesRepository {

    suspend fun saveFileBitmapMoreAndroid10(
        bitmap: Bitmap?,
        pathToSave: String,
        displayName: String
    ): Flow<Response<Uri>>

    suspend fun saveFileBitmapLessAndroid10(
        bitmap: Bitmap?,
        pathToSave: String,
        displayName: String
    ): Flow<Response<Uri>>

    suspend fun deleteFileBitmapMoreAndroid10(uri: Uri): Flow<Response<Boolean>>

    suspend fun deleteFileBitmapLessAndroid10(file: File): Flow<Response<Boolean>>

    @Deprecated("Only for migration")
    suspend fun moveFile(
        oldPath: String,
        newPath: String,
        fileName: String
    ): Flow<Response<Boolean>>

    suspend fun reloadMediaScanner(vararg filePath: String): Flow<Response<Boolean>>

    suspend fun loadItemsFilesMoreAndroid10(): Flow<Response<List<ItemFile>>>
    suspend fun loadItemsFilesLessAndroid10(filesPath: String): Flow<Response<List<ItemFile>>>
}
