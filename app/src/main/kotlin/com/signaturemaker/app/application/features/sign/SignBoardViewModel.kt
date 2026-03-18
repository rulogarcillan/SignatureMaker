package com.signaturemaker.app.application.features.sign

import android.graphics.Bitmap
import android.os.Build.VERSION
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.signaturemaker.app.domain.models.UriResponse
import com.signaturemaker.app.domain.usecase.SaveBitmapUseCase
import com.signaturemaker.app.domain.usecase.SaveBitmapUseCase.Params
import com.tuppersoft.skizo.kotlin.core.domain.exception.Failure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Created by Raúl Rodríguez Concepción on 16/09/2020.
 * raulrcs@gmail.com
 *
 * ViewModel for Sign Board feature
 * Uses Koin for dependency injection
 */
class SignBoardViewModel(
    private val saveBitmapUseCase: SaveBitmapUseCase
) : ViewModel() {

    private val _saveBitmap = Channel<UriResponse>(Channel.BUFFERED)
    val saveBitmap = _saveBitmap.receiveAsFlow()

    private val _failure = Channel<Failure>(Channel.BUFFERED)
    val failure = _failure.receiveAsFlow()

    fun saveFileBitmap(
        share: Boolean,
        bitmap: Bitmap? = null,
        pathToSave: String,
        displayName: String? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            saveBitmapUseCase.invoke(
                Params(
                    sdkInt = VERSION.SDK_INT,
                    pathToSave = pathToSave,
                    bitmap = bitmap,
                    displayName = displayName
                )
            ) {
                _failure.trySend(it)
            }.collect { result ->
                _saveBitmap.trySend(UriResponse(result, share))
            }
        }
    }
}
