package com.signaturemaker.app.domain.usecase

import android.graphics.BitmapFactory
import android.os.Build.VERSION_CODES
import com.signaturemaker.app.domain.repository.FilesRepository
import com.signaturemaker.app.domain.usecase.MoveAllFiles.Params
import com.tuppersoft.skizo.android.core.extension.logd
import com.tuppersoft.skizo.kotlin.core.customtypealias.OnFailure
import com.tuppersoft.skizo.kotlin.core.domain.baseusecase.GlobalUseCase
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
        val sdkInt: Int,
        val pathOldOfFiles: String,
        val pathNewOfFiles: String
    )

    override suspend fun run(params: Params, onFailure: OnFailure?): Boolean {
        val folder = File(params.pathOldOfFiles)
        if (folder.exists()) {
            folder.listFiles()?.forEach { file ->
                val name = file.name
                if ((name.contains(".png") || name.contains(".PNG"))
                    && name.startsWith("SM")
                ) {
                    if (params.sdkInt >= VERSION_CODES.Q) {
                        repository.saveFileBitmapMoreAndroid10(
                            BitmapFactory.decodeFile(file.path),
                            params.pathNewOfFiles,
                            name
                        ).collect {
                            "${file.path} is moved to ${params.pathNewOfFiles}".logd()
                            repository.deleteFileBitmapLessAndroid10(file).collect {
                                "${file.path} file removed".logd()
                            }
                        }
                    } else {
                        repository.moveFile(params.pathOldOfFiles, params.pathNewOfFiles, name).collect {
                            "${file.path} is moved to ${params.pathNewOfFiles}".logd()
                        }
                    }
                }
            }
        }

        val fileOld = File(params.pathOldOfFiles)
        if (fileOld.exists()) {
            fileOld.deleteRecursively()
        }

        return true
    }
}
