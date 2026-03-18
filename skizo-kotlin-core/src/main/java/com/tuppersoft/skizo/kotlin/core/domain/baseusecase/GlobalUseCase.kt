package com.tuppersoft.skizo.kotlin.core.domain.baseusecase

import com.tuppersoft.skizo.kotlin.core.customtypealias.OnFailure

/**
 * Created by Raúl Rodríguez Concepción on 17/08/2020.

 * raulrcs@gmail.com
 */
abstract class GlobalUseCase<out Type, in Params> where Type : Any? {

    protected abstract suspend fun run(params: Params, onFailure: OnFailure?): Type

    suspend fun invoke(params: Params, onFailure: OnFailure? = null): Type {
        return run(params, onFailure)
    }
}
