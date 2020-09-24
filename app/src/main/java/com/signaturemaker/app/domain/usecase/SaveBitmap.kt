package com.signaturemaker.app.domain.usecase

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build.VERSION_CODES
import com.signaturemaker.app.domain.repository.FilesRepository
import com.tuppersoft.skizo.core.domain.baseusecase.FlowGlobalUseCase
import com.tuppersoft.skizo.core.domain.response.Response
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by Raúl Rodríguez Concepción
 * raulrcs@gmail.com
 */
class SaveBitmap constructor(private val repository: FilesRepository) :
    FlowGlobalUseCase<Uri, SaveBitmap.Params>() {

    data class Params(
        val sdkInt: Int,
        val pathToSave: String,
        val bitmap: Bitmap? = null,
        val displayName: String? = null
    )

    override suspend fun run(params: Params): Flow<Response<Uri>> {
        return if (params.sdkInt >= VERSION_CODES.Q) {
            repository.saveFileBitmapMoreAndroid10(
                params.bitmap,
                params.pathToSave,
                addExtensionNamePng(params.displayName ?: generateName())
            )
        } else {
            repository.saveFileBitmapLessAndroid10(
                params.bitmap,
                params.pathToSave,
                addExtensionNamePng(params.displayName ?: generateName())
            )
        }
    }

    private fun generateName() = systemTime()

    private fun addExtensionNamePng(name: String) = "SM_$name.png"

    private fun systemTime(): String {
        val date = Date()
        val dfd = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        val dfh = SimpleDateFormat("HHmmss", Locale.getDefault())
        return dfd.format(date) + "_" + dfh.format(date)
    }

    /* fun saveSvgFile(content: String, name: String): Boolean {

         val file = File(Utils.path + name)
         createFolder(Utils.path)

         try {
             // file.createNewFile();
             //FileOutputStream os = new FileOutputStream(file);
             val out = BufferedWriter(FileWriter(file))

             try {
                 out.write(content)
             } catch (e: IOException) {
                 return false
             } finally {
                 out.close()
             }
         } catch (e: Exception) {
             return false
         }

         return true
     }*/
}
