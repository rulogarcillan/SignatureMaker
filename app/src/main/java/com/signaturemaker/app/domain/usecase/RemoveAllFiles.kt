package com.signaturemaker.app.domain.usecase

import com.signaturemaker.app.domain.usecase.RemoveFile.Params
import com.tuppersoft.skizo.kotlin.core.customtypealias.OnFailure
import com.tuppersoft.skizo.kotlin.core.domain.baseusecase.GlobalUseCase
import kotlinx.coroutines.flow.collect

/**
 * Created by Raúl Rodríguez Concepción
 * raulrcs@gmail.com
 */
class RemoveAllFiles constructor(
    private val removeFile: RemoveFile,
    private val getAllFiles: GetAllFiles
) :
    GlobalUseCase<Boolean, RemoveAllFiles.Params>() {

    data class Params(
        val sdkInt: Int,
        val pathOfFiles: String
    )

    override suspend fun run(params: Params, onFailure: OnFailure?): Boolean {

        getAllFiles.invoke(GetAllFiles.Params(params.sdkInt, params.pathOfFiles)).collect {
            it.forEach { itemFile ->
                removeFile.invoke(Params(params.sdkInt, itemFile))
            }
        }
        return true
    }
}
