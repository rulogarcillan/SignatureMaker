package com.signaturemaker.app.domain.usecase

import com.signaturemaker.app.domain.repository.FilesRepository
import com.signaturemaker.app.domain.usecase.MoveAllFiles.Params
import com.tuppersoft.skizo.core.customtypealias.OnFailure
import com.tuppersoft.skizo.core.domain.baseusecase.GlobalUseCase
import com.tuppersoft.skizo.core.extension.logd
import kotlinx.coroutines.flow.collect
import java.io.File

/**
 * Created by Raúl Rodríguez Concepción
 * raulrcs@gmail.com
 */
@Deprecated("Only for migration")
class MoveAllFiles constructor(private val repository: FilesRepository) :
    GlobalUseCase<Boolean, Params>() {

    data class Params(
        val pathOldOfFiles: String,
        val pathNewOfFiles: String
    )

    override suspend fun run(params: Params, onFailure: OnFailure?): Boolean {
        val folder = File(params.pathOldOfFiles)
        if (folder.exists()) {
            folder.listFiles()?.forEach { file ->
                val name = file.name
                if ((name.contains(".png")
                            || name.contains(".PNG")
                            || name.contains(".svg")
                            || name.contains(".SVG"))
                    && name.startsWith("SM")
                ) {
                    repository.moveFile(params.pathOldOfFiles, params.pathNewOfFiles, name).collect {
                        "${file.path} is moved to ${params.pathNewOfFiles}".logd()
                    }
                }
            }
        }

        repository.deleteFileBitmapLessAndroid10(File(params.pathOldOfFiles)).collect {
            "${params.pathOldOfFiles} folder removed".logd()

        }
        return true
    }
}
