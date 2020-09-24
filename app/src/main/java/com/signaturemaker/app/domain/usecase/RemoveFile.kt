package com.signaturemaker.app.domain.usecase

import android.os.Build.VERSION_CODES
import com.signaturemaker.app.domain.models.ItemFile
import com.signaturemaker.app.domain.repository.FilesRepository
import com.tuppersoft.skizo.core.domain.baseusecase.FlowGlobalUseCase
import com.tuppersoft.skizo.core.domain.response.Response
import com.tuppersoft.skizo.core.extension.logd
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import java.io.File

/**
 * Created by Raúl Rodríguez Concepción
 * raulrcs@gmail.com
 */
class RemoveFile constructor(private val repository: FilesRepository) :
    FlowGlobalUseCase<Boolean, RemoveFile.Params>() {

    data class Params(
        val sdkInt: Int,
        val item: ItemFile
    )

    override suspend fun run(params: Params): Flow<Response<Boolean>> {
        return if (params.sdkInt >= VERSION_CODES.Q) {
            val flow = repository.deleteFileBitmapMoreAndroid10(params.item.uri)
            flow.collect {
                "${params.item.uri.path} is deleted".logd()
            }
            flow
        } else {
            val file = File(params.item.uri.path ?: "")
            val flow = repository.deleteFileBitmapLessAndroid10(file)
            flow.collect {
                "${file.path} is deleted".logd()
            }
            flow
        }
    }
}

