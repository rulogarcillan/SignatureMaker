package com.signaturemaker.app.application.features.suggest

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.signaturemaker.app.domain.models.SuggestMessage
import com.signaturemaker.app.domain.usecase.SendSuggest
import com.signaturemaker.app.domain.usecase.SendSuggest.Params
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Raúl Rodríguez Concepción on 01/09/2020.

 * raulrcs@gmail.com
 */
class SuggestViewModel @ViewModelInject constructor(
    private val sendSuggest: SendSuggest
) : ViewModel() {

    private val _sendSussecs: MutableLiveData<Boolean> = MutableLiveData()
    val sendSussecs: LiveData<Boolean>
        get() = _sendSussecs

    fun sendSuggest(suggestMessage: SuggestMessage) {
        viewModelScope.launch(Dispatchers.IO) {
            sendSuggest.invoke(Params(suggestMessage)) {
                _sendSussecs.postValue(false)
            }.collect {
                _sendSussecs.postValue(true)
            }
        }
    }
}
