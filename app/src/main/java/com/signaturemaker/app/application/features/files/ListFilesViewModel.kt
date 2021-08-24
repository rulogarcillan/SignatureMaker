package com.signaturemaker.app.application.features.files

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.signaturemaker.app.domain.models.ItemFile
import com.signaturemaker.app.domain.usecase.GetAllFiles
import com.signaturemaker.app.domain.usecase.RemoveFile
import com.signaturemaker.app.domain.usecase.RemoveFile.Params
import com.tuppersoft.skizo.android.core.extension.logd
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Raúl Rodríguez Concepción on 17/09/2020.

 * raulrcs@gmail.com
 */

@HiltViewModel
class ListFilesViewModel @Inject constructor(
    private val getAllFiles: GetAllFiles,
    private val removeFile: RemoveFile
) : ViewModel() {

    private val _listFiles: MutableLiveData<MutableList<ItemFile>> = MutableLiveData()
    val listFiles: LiveData<List<ItemFile>> get() = _listFiles.map { it.toList() }

    fun getAllFiles(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getAllFiles.invoke(
                GetAllFiles.Params(
                    Build.VERSION.SDK_INT,
                    path
                )
            ).collect {
                _listFiles.postValue(it.toMutableList())
            }
        }
    }

    fun removeFile(itemFile: ItemFile) {
        GlobalScope.launch(Dispatchers.IO) {
            removeFile.invoke(Params(Build.VERSION.SDK_INT, itemFile)).collect {
                "Removed!!".logd()
            }
        }
    }
}
