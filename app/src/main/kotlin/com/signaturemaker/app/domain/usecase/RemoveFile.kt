package com.signaturemaker.app.domain.usecase

import android.os.Build.VERSION_CODES
import com.signaturemaker.app.domain.models.ItemFile
import com.signaturemaker.app.domain.repository.FilesRepository
import com.signaturemaker.app.domain.usecase.RemoveFile.Params
import com.tuppersoft.skizo.kotlin.core.domain.baseusecase.FlowGlobalUseCase
import com.tuppersoft.skizo.kotlin.core.domain.response.Response
import kotlinx.coroutines.flow.Flow
import java.io.File

/**
 * Created by Raúl Rodríguez Concepción
 * raulrcs@gmail.com
 */
class RemoveFile(private val repository: FilesRepository) :
    FlowGlobalUseCase<Boolean, Params>() {

    data class Params(
        val sdkInt: Int,
        val item: ItemFile,
        val path: String = ""
    )

    override suspend fun run(params: Params): Flow<Response<Boolean>> {
        return if (params.sdkInt >= VERSION_CODES.Q) {
            repository.deleteFileBitmapMoreAndroid10(params.item.uri)
        } else {
            val file = File(params.path + File.separator + params.item.uri.path?.split("/")?.last())
            repository.deleteFileBitmapLessAndroid10(file)
        }
    }
}
