package com.signaturemaker.app.application.features.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject

/**
 * Created by Raúl Rodríguez Concepción on 2019-12-20.
 * raulrcs@gmail.com
 */

class SettingViewModel @Inject constructor() : ViewModel() {

    val title: MutableLiveData<String> = MutableLiveData()
    val backButton: MutableLiveData<Boolean> = MutableLiveData()
    val cancelButton: MutableLiveData<Boolean> = MutableLiveData()
    val showToolbar: MutableLiveData<Boolean> = MutableLiveData()

    fun toolBarTitle(title: String) {
        this.title.value = title
    }

    fun backButton(flag: Boolean) {
        this.backButton.value = flag
    }

    fun cancelButton(flag: Boolean) {
        this.cancelButton.value = flag
    }

    fun showToolbar(flag: Boolean) {
        this.showToolbar.value = flag
    }
}
