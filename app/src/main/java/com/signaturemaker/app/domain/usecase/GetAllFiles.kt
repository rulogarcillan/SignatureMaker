package com.signaturemaker.app.domain.usecase

import android.os.Build.VERSION_CODES
import com.signaturemaker.app.domain.models.ItemFile
import com.signaturemaker.app.domain.repository.FilesRepository
import com.signaturemaker.app.domain.usecase.GetAllFiles.Params
import com.tuppersoft.skizo.kotlin.core.domain.baseusecase.FlowGlobalUseCase
import com.tuppersoft.skizo.kotlin.core.domain.response.Response
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Created by Raúl Rodríguez Concepción
 * raulrcs@gmail.com
 */
class GetAllFiles @Inject constructor(private val repository: FilesRepository) :
    FlowGlobalUseCase<List<ItemFile>, Params>() {

    data class Params(
        val sdkInt: Int,
        val pathOfFiles: String
    )

    override suspend fun run(params: Params): Flow<Response<List<ItemFile>>> {

        return if (params.sdkInt >= VERSION_CODES.Q) {
            repository.loadItemsFilesMoreAndroid10()
        } else {
            repository.loadItemsFilesLessAndroid10(params.pathOfFiles)
        }
    }
}
