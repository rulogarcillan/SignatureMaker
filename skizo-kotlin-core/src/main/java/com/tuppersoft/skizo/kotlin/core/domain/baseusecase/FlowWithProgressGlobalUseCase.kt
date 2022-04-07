package com.tuppersoft.skizo.kotlin.core.domain.baseusecase

import com.tuppersoft.skizo.kotlin.core.customtypealias.OnFailure
import com.tuppersoft.skizo.kotlin.core.customtypealias.OnProgress
import com.tuppersoft.skizo.kotlin.core.domain.response.ProgressResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

abstract class FlowWithProgressGlobalUseCase<out Type, in Params> where Type : Any? {

    protected abstract suspend fun run(params: Params): Flow<ProgressResponse<Type>>

    suspend fun invoke(params: Params, onFailure: OnFailure, onProgress: OnProgress): Flow<Type> {

        val result = run(params)
        return result.transform { m ->
            when (m) {
                is ProgressResponse.onSuccess -> emit(m.value)
                is ProgressResponse.onFailure -> onFailure(m.typeError)
                is ProgressResponse.onProgress -> onProgress(m.progress)
            }

        }
    }
}

