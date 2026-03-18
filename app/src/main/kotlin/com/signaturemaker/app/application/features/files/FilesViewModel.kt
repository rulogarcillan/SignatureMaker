package com.signaturemaker.app.application.features.files

import android.app.PendingIntent
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.signaturemaker.app.domain.models.ItemFile
import com.signaturemaker.app.domain.models.error.FileError
import com.signaturemaker.app.domain.usecase.GetAllFiles
import com.signaturemaker.app.domain.usecase.RemoveFile
import com.tuppersoft.skizo.android.core.extension.logd
import com.tuppersoft.skizo.android.core.extension.loge
import com.tuppersoft.skizo.kotlin.core.domain.exception.Failure
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for Files feature
 * Manages the state of the files list and handles file operations
 */
class FilesViewModel(
    private val getAllFiles: GetAllFiles,
    private val removeFile: RemoveFile
) : ViewModel() {

    private val _uiState = MutableStateFlow(FilesUiState())
    val uiState: StateFlow<FilesUiState> = _uiState.asStateFlow()

    private var currentPath: String = ""

    /**
     * Load all files from storage
     */
    fun loadFiles(path: String) {
        currentPath = path
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch(Dispatchers.IO) {
            getAllFiles.invoke(
                GetAllFiles.Params(
                    sdkInt = Build.VERSION.SDK_INT,
                    pathOfFiles = path
                )
            ) { failure ->
                _uiState.update { it.copy(isLoading = false, error = failure) }
            }.collect { response ->
                _uiState.update {
                    it.copy(
                        files = response.toImmutableList(),
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }

    /**
     * Remove a file from storage
     */
    fun removeFile(itemFile: ItemFile) {
        "removeFile called for: ${itemFile.name}".logd()

        // Primero eliminamos optimistamente de la UI
        _uiState.update { state ->
            state.copy(files = state.files.filter { it.uri != itemFile.uri }.toImmutableList())
        }

        // Luego intentamos eliminar del storage
        viewModelScope.launch(Dispatchers.IO) {
            try {
                removeFile.invoke(
                    RemoveFile.Params(
                        sdkInt = Build.VERSION.SDK_INT,
                        item = itemFile,
                        path = currentPath
                    )
                ).collect {
                    // File removed successfully from storage
                    "File deleted successfully from storage".logd()
                    _uiState.update { state ->
                        state.copy(deleteSuccess = true)
                    }
                }
            } catch (e: Exception) {
                "Error deleting file: ${e.message}".loge()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val recoverableSecurityException = e as? android.app.RecoverableSecurityException
                    if (recoverableSecurityException != null) {
                        "RecoverableSecurityException detected, requesting permission".logd()
                        // Restauramos el archivo en la UI ya que necesitamos permiso
                        _uiState.update { state ->
                            state.copy(
                                files = (state.files + itemFile).toImmutableList(),
                                pendingDeleteIntent = recoverableSecurityException.userAction.actionIntent,
                                pendingDeleteFile = itemFile
                            )
                        }
                        return@launch
                    }
                }
                // Otros errores - restauramos el archivo
                _uiState.update { state ->
                    state.copy(
                        files = (state.files + itemFile).toImmutableList(),
                        error = FileError.CreateError(e.message ?: "Error deleting file")
                    )
                }
            }
        }
    }

    /**
     * Clear pending delete intent after handling
     */
    fun clearPendingDeleteIntent() {
        _uiState.update { it.copy(pendingDeleteIntent = null, pendingDeleteFile = null) }
    }

    /**
     * Retry deletion after user grants permission
     * No elimina optimistamente porque el archivo ya debe estar removido de la UI
     */
    fun retryDelete() {
        "retryDelete called".logd()
        _uiState.value.pendingDeleteFile?.let { fileToDelete ->
            clearPendingDeleteIntent()

            // Volver a eliminar optimistamente de la UI
            _uiState.update { state ->
                state.copy(files = state.files.filter { it.uri != fileToDelete.uri }.toImmutableList())
            }

            // Intentar eliminar del storage (ahora con permiso)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    removeFile.invoke(
                        RemoveFile.Params(
                            sdkInt = Build.VERSION.SDK_INT,
                            item = fileToDelete,
                            path = currentPath
                        )
                    ).collect {
                        // File removed successfully from storage
                        "File deleted successfully after permission".logd()
                        _uiState.update { state ->
                            state.copy(deleteSuccess = true)
                        }
                    }
                } catch (e: Exception) {
                    "Error deleting file after permission: ${e.message}".loge()
                    // Si falla, restaurar el archivo
                    _uiState.update { state ->
                        state.copy(
                            files = (state.files + fileToDelete).toImmutableList(),
                            error = FileError.CreateError(e.message ?: "Error deleting file")
                        )
                    }
                }
            }
        }
    }

    /**
     * Clear any error state
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    /**
     * Clear delete success flag after showing snackbar
     */
    fun clearDeleteSuccess() {
        _uiState.update { it.copy(deleteSuccess = false) }
    }

    /**
     * Handle file dismissed (undo action expired)
     */
    fun onFileDismissed(itemFile: ItemFile) {
        removeFile(itemFile)
    }

    /**
     * Handle undo action
     */
    fun onUndoDelete(path: String) {
        loadFiles(path)
    }
}

/**
 * UI State for Files Screen
 */
data class FilesUiState(
    val files: ImmutableList<ItemFile> = persistentListOf(),
    val isLoading: Boolean = false,
    val error: Failure? = null,
    val pendingDeleteIntent: PendingIntent? = null,
    val pendingDeleteFile: ItemFile? = null,
    val deleteSuccess: Boolean = false
)
