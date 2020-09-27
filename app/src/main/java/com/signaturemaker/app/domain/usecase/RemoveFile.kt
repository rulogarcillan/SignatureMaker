package com.signaturemaker.app.domain.usecase

import com.signaturemaker.app.domain.models.ItemFile
import com.signaturemaker.app.domain.repository.FilesRepository
import com.signaturemaker.app.domain.usecase.RemoveFile.Params
import com.tuppersoft.skizo.android.core.extension.logd
import com.tuppersoft.skizo.kotlin.core.domain.baseusecase.FlowGlobalUseCase
import com.tuppersoft.skizo.kotlin.core.domain.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * Created by Raúl Rodríguez Concepción
 * raulrcs@gmail.com
 */
class RemoveFile constructor(private val repository: FilesRepository) :
    FlowGlobalUseCase<Boolean, Params>() {

    data class Params(
        val item: ItemFile
    )

    override suspend fun run(params: Params): Flow<Response<Boolean>> {
        val flow = repository.deleteFileBitmapFromUri(params.item.uri)
        flow.collect {
            "${params.item.uri.path} is deleted".logd()
        }
        return flow
    }
}

