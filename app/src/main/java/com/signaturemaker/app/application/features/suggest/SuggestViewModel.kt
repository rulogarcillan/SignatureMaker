package com.signaturemaker.app.application.features.suggest

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.signaturemaker.app.domain.models.SuggestMessage
import com.signaturemaker.app.domain.usecase.SendSuggest
import com.signaturemaker.app.domain.usecase.SendSuggest.Params
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Raúl Rodríguez Concepción on 01/09/2020.

 * raulrcs@gmail.com
 */
@HiltViewModel
class SuggestViewModel @Inject constructor(
    private val sendSuggest: SendSuggest
) : ViewModel() {

    private val _sendSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val sendSuccess: LiveData<Boolean>
        get() = _sendSuccess

    fun sendSuggest(suggestMessage: SuggestMessage) {
        viewModelScope.launch(Dispatchers.IO) {
            sendSuggest.invoke(Params(suggestMessage)) {
                _sendSuccess.postValue(false)
            }.collect {
                _sendSuccess.postValue(true)
            }
        }
    }
}
