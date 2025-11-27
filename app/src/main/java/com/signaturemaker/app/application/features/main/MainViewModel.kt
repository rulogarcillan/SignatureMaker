package com.signaturemaker.app.application.features.main

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.signaturemaker.app.domain.usecase.RemoveAllFiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * MainViewModel
 * Handles file operations for MainActivity
 */
class MainViewModel(
    private val removeAllFiles: RemoveAllFiles,
) : ViewModel() {

    fun removeAllFiles(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            removeAllFiles.invoke(
                RemoveAllFiles.Params(
                    sdkInt = Build.VERSION.SDK_INT,
                    pathOfFiles = path
                )
            )
        }
    }
}

