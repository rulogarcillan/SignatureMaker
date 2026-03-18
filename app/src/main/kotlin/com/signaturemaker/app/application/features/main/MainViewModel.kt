package com.signaturemaker.app.application.features.main

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.signaturemaker.app.domain.usecase.RemoveAllFilesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * MainViewModel
 * Handles file operations for MainActivity
 */
class MainViewModel(
    private val removeAllFiles: RemoveAllFilesUseCase,
) : ViewModel() {

    fun removeAllFiles(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            removeAllFiles.invoke(
                RemoveAllFilesUseCase.Params(
                    sdkInt = Build.VERSION.SDK_INT,
                    pathOfFiles = path
                )
            )
        }
    }
}
