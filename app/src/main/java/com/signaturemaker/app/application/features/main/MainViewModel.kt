package com.signaturemaker.app.application.features.main

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.signaturemaker.app.domain.usecase.MoveAllFiles
import com.signaturemaker.app.domain.usecase.RemoveAllFiles
import com.signaturemaker.app.domain.usecase.RemoveAllFiles.Params
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Raúl Rodríguez Concepción on 17/09/2020.

 * raulrcs@gmail.com
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val removeAllFiles: RemoveAllFiles,
    private val moveAllFiles: MoveAllFiles
) : ViewModel() {

    fun removeAllFiles(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            removeAllFiles.invoke(
                Params(
                    Build.VERSION.SDK_INT,
                    path
                )
            )
        }
    }

    fun moveAllFiles(oldPath: String, newPath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            moveAllFiles.invoke(MoveAllFiles.Params(Build.VERSION.SDK_INT, oldPath, newPath))
        }
    }
}
