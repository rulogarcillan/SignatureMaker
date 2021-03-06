package com.signaturemaker.app.domain.usecase

import android.os.Build.VERSION_CODES
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.domain.models.ItemFile
import com.signaturemaker.app.domain.repository.FilesRepository
import com.signaturemaker.app.domain.usecase.RemoveFile.Params
import com.tuppersoft.skizo.android.core.extension.logd
import com.tuppersoft.skizo.kotlin.core.domain.baseusecase.FlowGlobalUseCase
import com.tuppersoft.skizo.kotlin.core.domain.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import java.io.File
import javax.inject.Inject

/**
 * Created by Raúl Rodríguez Concepción
 * raulrcs@gmail.com
 */
class RemoveFile @Inject constructor(private val repository: FilesRepository) :
    FlowGlobalUseCase<Boolean, Params>() {

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
            val file = File(Utils.path + File.separator + params.item.uri.path?.split("/")?.last())
            val flow = repository.deleteFileBitmapLessAndroid10(file)
            flow.collect {
                "${file.path} is deleted".logd()
            }
            flow
        }
    }
}

