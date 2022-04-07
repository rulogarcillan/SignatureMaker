package com.tuppersoft.skizo.kotlin.core.domain.response

import com.tuppersoft.skizo.kotlin.core.domain.exception.Failure

sealed class ProgressResponse<out T> {
    data class onSuccess<out T>(val value: T) : ProgressResponse<T>()
    data class onFailure(val typeError: Failure) : ProgressResponse<Nothing>()
    data class onProgress(val progress: Int) : ProgressResponse<Nothing>()
}
