package com.signaturemaker.app.application.features.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

/**
 * Created by Raúl Rodríguez Concepción on 2019-12-20.
 * raulrcs@gmail.com
 */

class SharedViewModel @Inject constructor() : ViewModel() {

    private val _reloadFileList: MutableLiveData<Boolean> = MutableLiveData()
    val reloadFileList: LiveData<Boolean> get() = _reloadFileList

    fun reloadFileList() {
        _reloadFileList.value = true
    }
}
