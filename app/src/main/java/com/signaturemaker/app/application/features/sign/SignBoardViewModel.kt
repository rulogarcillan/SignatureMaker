package com.signaturemaker.app.application.features.sign

import android.graphics.Bitmap
import android.os.Build.VERSION
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.signaturemaker.app.application.core.platform.Event
import com.signaturemaker.app.domain.models.UriResponse
import com.signaturemaker.app.domain.usecase.SaveBitmap
import com.signaturemaker.app.domain.usecase.SaveBitmap.Params
import com.tuppersoft.skizo.kotlin.core.domain.exception.Failure
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Raúl Rodríguez Concepción on 16/09/2020.

 * raulrcs@gmail.com
 */
@HiltViewModel
class SignBoardViewModel @Inject constructor(
    private val saveBitmapUseCase: SaveBitmap
) : ViewModel() {

    private val _saveBitmap: MutableLiveData<Event<UriResponse>> = MutableLiveData()
    val saveBitmap: LiveData<Event<UriResponse>> get() = _saveBitmap

    private val _failure: MutableLiveData<Event<Failure>> = MutableLiveData()
    val failure: LiveData<Event<Failure>> get() = _failure

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
                _failure.postValue(Event(it))
            }.collect { result ->
                _saveBitmap.postValue(Event(UriResponse(result, share)))
            }
        }
    }
}
