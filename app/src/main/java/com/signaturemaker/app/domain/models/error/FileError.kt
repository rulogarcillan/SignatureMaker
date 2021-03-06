package com.signaturemaker.app.domain.models.error

import com.tuppersoft.skizo.kotlin.core.domain.exception.Failure.FeatureFailure

/**
 * Created by Raúl Rodríguez Concepción on 16/09/2020.

 * raulrcs@gmail.com
 */

class FileError {

    data class EmptyBitmap(val message: String) : FeatureFailure()
    data class CreateError(val message: String) : FeatureFailure()
}

