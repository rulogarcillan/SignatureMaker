package com.tuppersoft.skizo.kotlin.core.domain.baseusecase

import com.tuppersoft.skizo.kotlin.core.customtypealias.OnFailure
import com.tuppersoft.skizo.kotlin.core.domain.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

abstract class FlowGlobalUseCase<out Type, in Params> where Type : Any? {

    protected abstract suspend fun run(params: Params): Flow<Response<Type>>

    suspend fun invoke(params: Params, onFailure: OnFailure? = null): Flow<Type> {

        val result = run(params)
        return result.transform { r ->
            when (r) {
                is Response.onSuccess -> emit(r.value)
                is Response.onFailure -> {
                    onFailure?.invoke(r.typeError)
                }
            }

        }
    }
}
