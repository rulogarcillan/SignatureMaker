package com.signaturemaker.app.domain.models

import androidx.annotation.Keep

/**
 * Created by Raúl Rodríguez Concepción on 14/05/2020.

 * raulrcs@gmail.com
 */
@Keep
data class SuggestMessage(
    val email: String,
    val message: String
)
