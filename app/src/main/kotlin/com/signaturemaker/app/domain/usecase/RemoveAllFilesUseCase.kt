package com.signaturemaker.app.domain.usecase

import com.signaturemaker.app.domain.usecase.RemoveFileUseCase.Params
import com.tuppersoft.skizo.kotlin.core.customtypealias.OnFailure
import com.tuppersoft.skizo.kotlin.core.domain.baseusecase.GlobalUseCase
import kotlinx.coroutines.flow.collect

/**
 * Created by Raúl Rodríguez Concepción
 * raulrcs@gmail.com
 */
class RemoveAllFilesUseCase(
    private val removeFile: RemoveFileUseCase,
    private val getAllFiles: GetAllFilesUseCase
) :
    GlobalUseCase<Boolean, RemoveAllFilesUseCase.Params>() {

    data class Params(
        val sdkInt: Int,
        val pathOfFiles: String
    )

    override suspend fun run(params: Params, onFailure: OnFailure?): Boolean {
        getAllFiles.invoke(GetAllFilesUseCase.Params(params.sdkInt, params.pathOfFiles)).collect { fileListResponse ->
            fileListResponse.forEach { itemFile ->
                // Wait for each file to be deleted before continuing
                removeFile.invoke(Params(params.sdkInt, itemFile, params.pathOfFiles)).collect { /* Result handled internally */ }
            }
        }
        return true
    }
}
