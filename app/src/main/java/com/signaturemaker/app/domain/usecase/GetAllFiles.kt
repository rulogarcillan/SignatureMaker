package com.signaturemaker.app.domain.usecase

import android.net.Uri
import android.os.Build.VERSION_CODES
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.domain.models.ItemFile
import com.signaturemaker.app.domain.repository.FilesRepository
import com.signaturemaker.app.domain.usecase.GetAllFiles.Params
import com.tuppersoft.skizo.kotlin.core.domain.baseusecase.FlowGlobalUseCase
import com.tuppersoft.skizo.kotlin.core.domain.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by Raúl Rodríguez Concepción
 * raulrcs@gmail.com
 */
class GetAllFiles constructor(private val repository: FilesRepository) :
    FlowGlobalUseCase<List<ItemFile>, Params>() {

    data class Params(
        val sdkInt: Int,
        val pathOfFiles: String
    )

    override suspend fun run(params: Params): Flow<Response<List<ItemFile>>> {

        return if (params.sdkInt >= VERSION_CODES.Q) {
            repository.loadItemsFilesMoreAndroid10()
        } else {
            repository.loadItemsFilesLessAndroid10(params.pathOfFiles).map {
                when (it) {
                    is Response.onSuccess -> {
                        val itemListFiles: MutableList<ItemFile> = mutableListOf()
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                        it.value.forEach { file ->

                            val myDate = Date(file.lastModified())
                            val tam = (file.length() / 1024).toString() + " KB"
                            val name = file.name

                            if ((name.contains(".png") || name.contains(".PNG") || name.contains(".svg") || name.contains(
                                    ".SVG"
                                )) && name.startsWith(
                                    "SM_"
                                )
                            ) {
                                itemListFiles.add(ItemFile(Uri.fromFile(file), name, dateFormat.format(myDate), tam))
                            }

                        }
                        Utils.sort(itemListFiles, Utils.sortOrder)

                        Response.onSuccess(itemListFiles.toList())
                    }
                    is Response.onFailure -> Response.onSuccess(emptyList())
                }

            }
        }
    }
}
