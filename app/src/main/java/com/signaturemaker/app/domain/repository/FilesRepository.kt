package com.signaturemaker.app.domain.repository

import com.tuppersoft.skizo.core.domain.response.Response
import kotlinx.coroutines.flow.Flow

interface FilesRepository {

    suspend fun saveFileMoreAndroid10(): Flow<Response<Boolean>>

    suspend fun saveFileLessAndroid10(): Flow<Response<Boolean>>

    suspend fun deleteFileMoreAndroid10(): Flow<Response<Boolean>>

    suspend fun deleteFileLessAndroid10(): Flow<Response<Boolean>>

    suspend fun ReloadMediaScanner(filePath: String): Flow<Response<Boolean>>
}
