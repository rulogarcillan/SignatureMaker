package com.signaturemaker.app.domain.models

import java.io.Serializable

/**
 * Created by Raúl Rodríguez Concepción on 14/05/2020.

 * raulrcs@gmail.com
 */
data class SuggestMessage(var email: String,
    var message: String) : Serializable
