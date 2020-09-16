package com.signaturemaker.app.data.repositories

import android.content.Context
import android.media.MediaScannerConnection
import com.signaturemaker.app.domain.repository.FilesRepository
import com.tuppersoft.skizo.core.domain.response.Response
import com.tuppersoft.skizo.core.extension.logd
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FilesRepositoryImp @Inject constructor(@ApplicationContext val appContext: Context) : FilesRepository {

    override suspend fun saveFileMoreAndroid10(): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveFileLessAndroid10(): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFileMoreAndroid10(): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFileLessAndroid10(): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun ReloadMediaScanner(filePath: String): Flow<Response<Boolean>> {

        return flow {

            MediaScannerConnection.scanFile(
                appContext, arrayOf(filePath), null
            ) { path, _ ->
                "Scan complete for: $path".logd()
            }

            emit(Response.onSuccess(true))
        }
    }
}
